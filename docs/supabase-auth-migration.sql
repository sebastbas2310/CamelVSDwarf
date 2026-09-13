alter table public.app_users
    alter column password_hash drop not null;

alter table public.app_users
    add column if not exists supabase_user_id uuid;

create unique index if not exists uk_app_users_supabase_user_id
    on public.app_users (supabase_user_id)
    where supabase_user_id is not null;

do $$
begin
    if not exists (
        select 1
        from pg_constraint
        where conname = 'app_users_supabase_user_fk'
    ) then
        alter table public.app_users
            add constraint app_users_supabase_user_fk
            foreign key (supabase_user_id)
            references auth.users(id)
            on delete cascade;
    end if;
end
$$;

comment on column public.app_users.supabase_user_id is
    'UUID from Supabase Auth auth.users.id';
