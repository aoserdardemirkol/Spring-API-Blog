-- Table: public.users

-- DROP TABLE public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    gender integer,
    username character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (user_id)
)

TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to postgres;

-- Table: public.tag

-- DROP TABLE public.tag;

CREATE TABLE IF NOT EXISTS public.tag
(
    tag_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT tag_pkey PRIMARY KEY (tag_id)
)

TABLESPACE pg_default;

ALTER TABLE public.tag
    OWNER to postgres;

-- Table: public.entry

-- DROP TABLE public.entry;

CREATE TABLE IF NOT EXISTS public.entry
(
    entry_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    content character varying(255) COLLATE pg_catalog."default",
    create_date timestamp without time zone,
    title character varying(255) COLLATE pg_catalog."default",
    update_date timestamp without time zone,
    author_id character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT entry_pkey PRIMARY KEY (entry_id),
    CONSTRAINT fk8cj4p59pb3imifrs3khagurjg FOREIGN KEY (author_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.entry
    OWNER to postgres;

-- Table: public.entry_tags

-- DROP TABLE public.entry_tags;

CREATE TABLE IF NOT EXISTS public.entry_tags
(
    entry_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    tag_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT fknsh3miwca48y82dgvmj54qu47 FOREIGN KEY (entry_id)
        REFERENCES public.entry (entry_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fksv6gpt2u397jhon6lwe4iw0j6 FOREIGN KEY (tag_id)
        REFERENCES public.tag (tag_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.entry_tags
    OWNER to postgres;

-- Table: public.comment

-- DROP TABLE public.comment;

CREATE TABLE IF NOT EXISTS public.comment
(
    comment_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    content character varying(255) COLLATE pg_catalog."default",
    create_date timestamp without time zone,
    update_date timestamp without time zone,
    entry_id character varying(255) COLLATE pg_catalog."default",
    author_id character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT comment_pkey PRIMARY KEY (comment_id),
    CONSTRAINT fkb2vr0m6nf3xd38gsicgthk62q FOREIGN KEY (entry_id)
        REFERENCES public.entry (entry_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkir20vhrx08eh4itgpbfxip0s1 FOREIGN KEY (author_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.comment
    OWNER to postgres;