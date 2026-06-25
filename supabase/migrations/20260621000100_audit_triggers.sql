-- Migration: Add secure audit triggers and helper functions
-- Version: 20260621000100_audit_triggers.sql

-- Enable pgcrypto extension for secure hashing (digest function)
create extension if not exists pgcrypto;

-------------------------------------------------------------------------------
-- SECURE UTILITY FUNCTION: log_user_action
-- Explicit utility procedure to log targeted user and client transactions.
-- Hashes client_ip and user_agent using SHA256 for privacy compliance (GDPR/HIPAA).
-------------------------------------------------------------------------------
create or replace function public.log_user_action(
    p_actor_user_id uuid,
    p_action text,
    p_resource_type text,
    p_resource_id text,
    p_client_ip text,
    p_user_agent text,
    p_metadata jsonb default '{}'::jsonb
)
returns uuid as $$
declare
    v_audit_id uuid;
    v_actor_role text;
    v_ip_hash text := null;
    v_ua_hash text := null;
begin
    -- Retrieve the actor's role if authenticated
    if p_actor_user_id is not null then
        select role into v_actor_role from public.profiles where id = p_actor_user_id;
    else
        v_actor_role := 'anonymous';
    end if;

    -- Hash sensitive client identifiers using pgcrypto for strict data privacy
    if p_client_ip is not null then
        v_ip_hash := encode(digest(p_client_ip, 'sha256'), 'hex');
    end if;
    
    if p_user_agent is not null then
        v_ua_hash := encode(digest(p_user_agent, 'sha256'), 'hex');
    end if;

    insert into public.audit_logs (
        actor_user_id,
        actor_role,
        action,
        resource_type,
        resource_id,
        ip_hash,
        user_agent_hash,
        metadata
    ) values (
        p_actor_user_id,
        coalesce(v_actor_role, 'user'),
        p_action,
        p_resource_type,
        p_resource_id,
        v_ip_hash,
        v_ua_hash,
        p_metadata
    )
    returning id into v_audit_id;

    return v_audit_id;
end;
$$ language plpgsql security definer;

-------------------------------------------------------------------------------
-- SYSTEM TRIGGER FUNCTION: audit_sensitive_record_changes
-- Automates secure logging for all sensitive health, medical, and care records
-------------------------------------------------------------------------------
create or replace function public.audit_sensitive_record_changes()
returns trigger as $$
declare
    v_actor_id uuid;
    v_action text;
    v_res_id text;
    v_meta jsonb := '{}'::jsonb;
begin
    -- Determine the current authenticated user via Supabase JWT UID
    v_actor_id := auth.uid();
    
    -- Determine current database operation
    if (TG_OP = 'INSERT') then
        v_action := 'INSERT_RECORD';
        v_res_id := new.id::text;
    elsif (TG_OP = 'UPDATE') then
        v_action := 'UPDATE_RECORD';
        v_res_id := old.id::text;
        -- Capture high level context (without logging any user-defined decrypted text)
        v_meta := jsonb_build_object(
            'table', TG_TABLE_NAME,
            'schema', TG_TABLE_SCHEMA,
            'client_triggered', true
        );
    elsif (TG_OP = 'DELETE') then
        v_action := 'DELETE_RECORD';
        v_res_id := old.id::text;
    end if;

    -- Log action using secure log_user_action helper
    perform public.log_user_action(
        v_actor_id,
        v_action,
        TG_TABLE_NAME::text,
        v_res_id,
        -- Dynamically extract client IP and user-agent headers from current transaction context
        nullif(current_setting('request.headers', true)::json->>'x-forwarded-for', ''),
        nullif(current_setting('request.headers', true)::json->>'user-agent', ''),
        v_meta
    );

    if (TG_OP = 'DELETE') then
        return old;
    else
        return new;
    end if;
end;
$$ language plpgsql security definer;

-------------------------------------------------------------------------------
-- BIND AUTOMATED AUDIT TRIGGERS TO SENSITIVE TABLES
-------------------------------------------------------------------------------
-- Drop existing triggers to support clean re-entrance
drop trigger if exists audit_health_profiles_trigger on public.health_profiles;
create trigger audit_health_profiles_trigger
after insert or update or delete on public.health_profiles
for each row execute procedure public.audit_sensitive_record_changes();

drop trigger if exists audit_period_logs_trigger on public.period_logs;
create trigger audit_period_logs_trigger
after insert or update or delete on public.period_logs
for each row execute procedure public.audit_sensitive_record_changes();

drop trigger if exists audit_behaviour_logs_trigger on public.behaviour_logs;
create trigger audit_behaviour_logs_trigger
after insert or update or delete on public.behaviour_logs
for each row execute procedure public.audit_sensitive_record_changes();

drop trigger if exists audit_medical_journal_entries_trigger on public.medical_journal_entries;
create trigger audit_medical_journal_entries_trigger
after insert or update or delete on public.medical_journal_entries
for each row execute procedure public.audit_sensitive_record_changes();

drop trigger if exists audit_medical_reminders_trigger on public.medical_reminders;
create trigger audit_medical_reminders_trigger
after insert or update or delete on public.medical_reminders
for each row execute procedure public.audit_sensitive_record_changes();

drop trigger if exists audit_cup_care_logs_trigger on public.cup_care_logs;
create trigger audit_cup_care_logs_trigger
after insert or update or delete on public.cup_care_logs
for each row execute procedure public.audit_sensitive_record_changes();
