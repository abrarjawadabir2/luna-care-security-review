-- LunaCare Supabase Schema Initialization Migration-- Created: 2026-06-22 00:00:00 (Local time: 2026-06-21)-- Version: 20260621000000_init_schema.sql

-- Enable uuid-ossp if not already enabled
create extension if not exists "uuid-ossp";

-------------------------------------------------------------------------------
-- 1. PROFILES TABLE
-- References auth.users for secure Supabase Auth integration
-------------------------------------------------------------------------------
create table public.profiles (
    id uuid primary key references auth.users on delete cascade,
    display_name text not null,
    user_mode text not null check (user_mode in ('SELF_TRACKING', 'SUPPORT_MODE', 'EDUCATION_ONLY')),
    gender_mode text not null check (gender_mode in ('FEMALE', 'MALE', 'OTHER', 'PREFER_NOT_TO_SAY')),
    pronoun text not null check (pronoun in ('SHE_HER', 'HE_HIM', 'THEY_THEM', 'CUSTOM', 'PREFER_NOT_TO_SAY')),
    custom_pronoun text,
    body_relevant_mode text not null check (body_relevant_mode in ('MENSTRUATES', 'DOES_NOT_MENSTRUATE', 'NOT_SURE', 'PREFER_NOT_TO_SAY')),
    support_relationship text check (support_relationship in ('WIFE', 'MOTHER', 'DAUGHTER', 'GIRLFRIEND', 'FEMALE_PARTNER', 'SISTER', 'FRIEND', 'OTHER')),
    religion text check (religion in ('ISLAM', 'HINDU', 'CHRISTIAN', 'BUDDHIST', 'OTHER', 'PREFER_NOT_TO_SAY')),
    country text,
    region text,
    city text,
    location_privacy_mode text not null default 'OFF' check (location_privacy_mode in ('OFF', 'ON_DEVICE_ONLY', 'APPROXIMATE_REGION', 'TEMPORARY_EXACT')),
    last_location_permission_status text,
    consent_confirmed boolean not null default false,
    shared_tracking_consent boolean not null default false,
    behaviour_focuses text[] not null default '{}',
    average_cycle_length integer not null default 28,
    average_period_length integer not null default 5,
    visible_status boolean not null default true,
    dashboard_layout_version text not null default 'new',
    role text not null default 'user' check (role in ('user', 'premium_user', 'moderator', 'medical_content_reviewer', 'support_agent', 'admin', 'super_admin')),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-------------------------------------------------------------------------------
-- 2. HEALTH PROFILES TABLE
-- Stores selected self-reported conditions and client-side encrypted notes
-------------------------------------------------------------------------------
create table public.health_profiles (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references public.profiles(id) on delete cascade,
    selected_conditions text[] not null default '{}',
    notes_encrypted text, -- Clinically sensitive - client-side encrypted only
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-------------------------------------------------------------------------------
-- 3. PERIOD LOGS TABLE
-- Tracks cycle start dates and end dates with symptoms and client-side encrypted notes
-------------------------------------------------------------------------------
create table public.period_logs (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references public.profiles(id) on delete cascade,
    start_date date not null,
    end_date date,
    flow_level text,
    symptoms text[] not null default '{}',
    notes_encrypted text, -- Client-side encrypted notes
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-------------------------------------------------------------------------------
-- 4. BEHAVIOUR LOGS TABLE
-- Tracks daily mood, hydration, sleep quality, pain levels and other behavioural parameters
-------------------------------------------------------------------------------
create table public.behaviour_logs (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references public.profiles(id) on delete cascade,
    log_date date not null,
    mood text not null,
    stress_level integer check (stress_level >= 0 and stress_level <= 10),
    anxiety_level integer check (anxiety_level >= 0 and anxiety_level <= 10),
    sleep_hours numeric(4,2),
    sleep_quality integer check (sleep_quality >= 0 and sleep_quality <= 10),
    pain_level integer check (pain_level >= 0 and pain_level <= 10),
    energy_level integer check (energy_level >= 0 and energy_level <= 10),
    hydration_level text,
    food_craving text,
    caffeine_intake text,
    movement text,
    study_work_pressure integer check (study_work_pressure >= 0 and study_work_pressure <= 10),
    relationship_stress integer check (relationship_stress >= 0 and relationship_stress <= 10),
    social_media_overload integer check (social_media_overload >= 0 and social_media_overload <= 10),
    flow_level text,
    symptoms text[] not null default '{}',
    notes_encrypted text, -- Client-side encrypted notes
    flags text[] not null default '{}',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    unique(user_id, log_date)
);

-------------------------------------------------------------------------------
-- 5. MEDICAL JOURNAL ENTRIES TABLE
-- Encrypted medical records, appointments and medication trackers
-------------------------------------------------------------------------------
create table public.medical_journal_entries (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references public.profiles(id) on delete cascade,
    entry_date date not null,
    category text not null,
    title text not null,
    symptoms text[] not null default '{}',
    pain_level integer check (pain_level >= 0 and pain_level <= 10),
    mood text not null,
    flow_level text,
    medicines_taken text,
    doctor_visit boolean not null default false,
    next_appointment date,
    notes_encrypted text, -- Clinically sensitive - client-side encrypted only
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-------------------------------------------------------------------------------
-- 6. MEDICAL REMINDERS TABLE
-- Tracks user-created medication and doctor's reminders
-------------------------------------------------------------------------------
create table public.medical_reminders (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references public.profiles(id) on delete cascade,
    title text not null,
    reminder_type text not null,
    reminder_time text not null,
    repeat_rule text not null,
    enabled boolean not null default true,
    notes_encrypted text, -- Client-side encrypted notes
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-------------------------------------------------------------------------------
-- 7. CUP CARE LOGS TABLE
-- Tracks menstrual cup placement time, emptied times and hygiene hygiene status
-------------------------------------------------------------------------------
create table public.cup_care_logs (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references public.profiles(id) on delete cascade,
    inserted_at timestamptz,
    emptied_at timestamptz,
    cleaned_today boolean not null default false,
    discomfort_level integer check (discomfort_level >= 0 and discomfort_level <= 10),
    leakage_issue boolean not null default false,
    notes_encrypted text, -- Client-side encrypted notes
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-------------------------------------------------------------------------------
-- 8. BOOKMARKS TABLE
-- Allows users to save health guides, diagnostic rules and awareness logs
-------------------------------------------------------------------------------
create table public.bookmarks (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references public.profiles(id) on delete cascade,
    item_type text not null,
    item_id text not null,
    title text not null,
    created_at timestamptz not null default now(),
    unique(user_id, item_type, item_id)
);

-------------------------------------------------------------------------------
-- 9. NOTIFICATIONS TABLE
-- System, medical cycle, mental wellness and alert notifications
-------------------------------------------------------------------------------
create table public.notifications (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references public.profiles(id) on delete cascade,
    title text not null,
    message text not null,
    type text not null,
    read boolean not null default false,
    created_at timestamptz not null default now()
);

-------------------------------------------------------------------------------
-- 10. AI USAGE TABLE
-- Tracks premium AI credits, tokens used and tier validation limits
-------------------------------------------------------------------------------
create table public.ai_usage (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references public.profiles(id) on delete cascade,
    month text not null, -- format 'YYYY-MM'
    tokens_used integer not null default 0,
    messages_used integer not null default 0,
    tier text not null default 'free' check (tier in ('free', 'premium')),
    updated_at timestamptz not null default now(),
    unique(user_id, month)
);

-------------------------------------------------------------------------------
-- 11. AUDIT LOGS TABLE
-- Append-only system logging for role changes, database operations, and user changes
-------------------------------------------------------------------------------
create table public.audit_logs (
    id uuid primary key default gen_random_uuid(),
    actor_user_id uuid references public.profiles(id) on delete set null,
    actor_role text not null,
    action text not null,
    resource_type text not null,
    resource_id text,
    ip_hash text,
    user_agent_hash text,
    metadata jsonb,
    created_at timestamptz not null default now()
);

-------------------------------------------------------------------------------
-- AUTOMATIC TIMESTAMPS HANDLERS
-- Keeps updated_at field updated on edits
-------------------------------------------------------------------------------
create or replace function public.update_updated_at_column()
returns trigger as $$
begin
    new.updated_at = now();
    return new;
end;
$$ language plpgsql;

create trigger set_profiles_updated_at before update on public.profiles or each row execute procedure public.update_updated_at_column();
create trigger set_health_profiles_updated_at before update on public.health_profiles for each row execute procedure public.update_updated_at_column();
create trigger set_period_logs_updated_at before update on public.period_logs for each row execute procedure public.update_updated_at_column();
create trigger set_behaviour_logs_updated_at before update on public.behaviour_logs for each row execute procedure public.update_updated_at_column();
create trigger set_medical_journal_entries_updated_at before update on public.medical_journal_entries for each row execute procedure public.update_updated_at_column();
create trigger set_medical_reminders_updated_at before update on public.medical_reminders for each row execute procedure public.update_updated_at_column();
create trigger set_cup_care_logs_updated_at before update on public.cup_care_logs for each row execute procedure public.update_updated_at_column();
create trigger set_ai_usage_updated_at before update on public.ai_usage for each row execute procedure public.update_updated_at_column();


-------------------------------------------------------------------------------
-- AUTH TRIGGERS FOR NEW USERS
-- Automatically inserts a record in public.profiles when someone registers via auth
-------------------------------------------------------------------------------
create or replace function public.handle_new_user()
returns trigger as $$
begin
    insert into public.profiles (
        id,
        display_name,
        user_mode,
        gender_mode,
        pronoun,
        body_relevant_mode,
        location_privacy_mode,
        role
    ) values (
        new.id,
        coalesce(new.raw_user_meta_data->>'display_name', new.email),
        'EDUCATION_ONLY', -- Safe Default userMode
        'PREFER_NOT_TO_SAY',
        'PREFER_NOT_TO_SAY',
        'PREFER_NOT_TO_SAY',
        'OFF', -- Safe default privacy-GPS setting
        'user' -- Regular user role
    );
    return new;
end;
$$ language plpgsql security definer;

create trigger on_auth_user_created
    after insert on auth.users
    for each row execute procedure public.handle_new_user();


-------------------------------------------------------------------------------
-- ROW LEVEL SECURITY (RLS) POLICIES
-- Strict user sandbox, absolute medical isolation, role checks
-------------------------------------------------------------------------------

-- 1. Enable RLS on all tables
alter table public.profiles enable row level security;
alter table public.health_profiles enable row level security;
alter table public.period_logs enable row level security;
alter table public.behaviour_logs enable row level security;
alter table public.medical_journal_entries enable row level security;
alter table public.medical_reminders enable row level security;
alter table public.cup_care_logs enable row level security;
alter table public.bookmarks enable row level security;
alter table public.notifications enable row level security;
alter table public.ai_usage enable row level security;
alter table public.audit_logs enable row level security;

-- 2. Helper function to fetch the role of current caller
-- Security Definer securely bypasses RLS for this specific check to prevent recursion
create or replace function public.get_current_user_role()
returns text as $$
    select role from public.profiles where id = auth.uid();
$$ language sql security definer;

-- 3. PROFILES POLICIES
create policy "Users can select own profile."
    on public.profiles for select
    using (auth.uid() = id or public.get_current_user_role() in ('admin', 'super_admin', 'support_agent'));

create policy "Users can update own profile."
    on public.profiles for update
    using (auth.uid() = id or public.get_current_user_role() = 'super_admin');

create policy "Admins can delete profiles."
    on public.profiles for delete
    using (public.get_current_user_role() = 'super_admin');

-- 4. PRIVATE CLINICAL/PERSONAL DATA ISOLATION (No Admins can bypass RLS for medical journals)
-- health_profiles policies
create policy "Users can select own health profiles"
    on public.health_profiles for select
    using (auth.uid() = user_id);

create policy "Users can insert own health profiles"
    on public.health_profiles for insert
    with check (auth.uid() = user_id);

create policy "Users can update own health profiles"
    on public.health_profiles for update
    using (auth.uid() = user_id);

create policy "Users can delete own health profiles"
    on public.health_profiles for delete
    using (auth.uid() = user_id);

-- period_logs policies
create policy "Users can select own period logs"
    on public.period_logs for select
    using (auth.uid() = user_id);

create policy "Users can insert own period logs"
    on public.period_logs for insert
    with check (auth.uid() = user_id);

create policy "Users can update own period logs"
    on public.period_logs for update
    using (auth.uid() = user_id);

create policy "Users can delete own period logs"
    on public.period_logs for delete
    using (auth.uid() = user_id);

-- behaviour_logs policies
create policy "Users can select own behaviour logs"
    on public.behaviour_logs for select
    using (auth.uid() = user_id);

create policy "Users can insert own behaviour logs"
    on public.behaviour_logs for insert
    with check (auth.uid() = user_id);

create policy "Users can update own behaviour logs"
    on public.behaviour_logs for update
    using (auth.uid() = user_id);

create policy "Users can delete own behaviour logs"
    on public.behaviour_logs for delete
    using (auth.uid() = user_id);

-- medical_journal_entries policies
create policy "Users can select own medical journal entries"
    on public.medical_journal_entries for select
    using (auth.uid() = user_id);

create policy "Users can insert own medical journal entries"
    on public.medical_journal_entries for insert
    with check (auth.uid() = user_id);

create policy "Users can update own medical journal entries"
    on public.medical_journal_entries for update
    using (auth.uid() = user_id);

create policy "Users can delete own medical journal entries"
    on public.medical_journal_entries for delete
    using (auth.uid() = user_id);

-- medical_reminders policies
create policy "Users can select own medical reminders"
    on public.medical_reminders for select
    using (auth.uid() = user_id);

create policy "Users can insert own medical reminders"
    on public.medical_reminders for insert
    with check (auth.uid() = user_id);

create policy "Users can update own medical reminders"
    on public.medical_reminders for update
    using (auth.uid() = user_id);

create policy "Users can delete own medical reminders"
    on public.medical_reminders for delete
    using (auth.uid() = user_id);

-- cup_care_logs policies
create policy "Users can select own cup care logs"
    on public.cup_care_logs for select
    using (auth.uid() = user_id);

create policy "Users can insert own cup care logs"
    on public.cup_care_logs for insert
    with check (auth.uid() = user_id);

create policy "Users can update own cup care logs"
    on public.cup_care_logs for update
    using (auth.uid() = user_id);

create policy "Users can delete own cup care logs"
    on public.cup_care_logs for delete
    using (auth.uid() = user_id);

-- bookmarks policies
create policy "Users can manage own bookmarks"
    on public.bookmarks for all
    using (auth.uid() = user_id);

-- notifications policies
create policy "Users can read own notifications"
    on public.notifications for select
    using (auth.uid() = user_id or public.get_current_user_role() in ('admin', 'super_admin'));

create policy "Users/Agents can insert notifications"
    on public.notifications for insert
    with check (auth.uid() = user_id or public.get_current_user_role() in ('admin', 'super_admin', 'support_agent'));

create policy "Users can update own notifications"
    on public.notifications for update
    using (auth.uid() = user_id or public.get_current_user_role() in ('admin', 'super_admin'));

-- ai_usage policies
create policy "Users/Admins can view token usage"
    on public.ai_usage for select
    using (auth.uid() = user_id or public.get_current_user_role() in ('admin', 'super_admin'));

create policy "Users can manage own usage logs"
    on public.ai_usage for all
    using (auth.uid() = user_id);

-- audit_logs policies (Strict append-only, absolutely no updates or deletes)
create policy "Audited users / System can write audit logs"
    on public.audit_logs for insert
    with check (auth.uid() = actor_user_id or actor_user_id is null);

create policy "Only administrators or higher can select audit logs"
    on public.audit_logs for select
    using (public.get_current_user_role() in ('admin', 'super_admin'));
