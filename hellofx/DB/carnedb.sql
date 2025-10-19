CREATE SCHEMA IF NOT EXISTS "public";

CREATE  TABLE "public".departamento ( 
	id_departamento      serial  NOT NULL  ,
	nombre               text    ,
	CONSTRAINT pk_departamento PRIMARY KEY ( id_departamento )
 );

CREATE  TABLE "public".especies ( 
	id_especie           serial  NOT NULL  ,
	nombre               text    ,
	CONSTRAINT pk_especies PRIMARY KEY ( id_especie )
 );

CREATE  TABLE "public".lotes ( 
	id_lote              integer  NOT NULL  ,
	cantidad             integer    ,
	id_especie           integer    ,
	CONSTRAINT pk_lotes PRIMARY KEY ( id_lote )
 );

CREATE  TABLE "public".municipio ( 
	id_municipio         serial  NOT NULL  ,
	nombre               text    ,
	id_departamento      integer    ,
	CONSTRAINT pk_municipio PRIMARY KEY ( id_municipio )
 );

CREATE  TABLE "public".recomendacion ( 
	id_recomendacion     serial  NOT NULL  ,
	nombre               text    ,
	descripcion          text    ,
	url                  text    ,
	CONSTRAINT pk_recomendacion PRIMARY KEY ( id_recomendacion )
 );

CREATE  TABLE "public".rol ( 
	id_rol               serial  NOT NULL  ,
	rol                  text    ,
	descripcion          text    ,
	permisos             text[]    ,
	CONSTRAINT pk_rol PRIMARY KEY ( id_rol )
 );

CREATE  TABLE "public"."user" ( 
	id_user              serial  NOT NULL  ,
	nombre               text  NOT NULL  ,
	email                text  NOT NULL  ,
	telefono             text  NOT NULL  ,
	cedula               text  NOT NULL  ,
	contraseña           text    ,
	id_rol               integer    ,
	CONSTRAINT pk_user PRIMARY KEY ( id_user )
 );

CREATE  TABLE "public".ciudad ( 
	id_ciudad            serial  NOT NULL  ,
	nombre               text    ,
	id_municipio         integer    ,
	CONSTRAINT pk_ciudad PRIMARY KEY ( id_ciudad )
 );

CREATE  TABLE "public".granja ( 
	id_granja            integer  NOT NULL  ,
	nombre               text    ,
	ubicacion            text    ,
	especies_cultivadas  text[]    ,
	id_ciudad            integer    ,
	CONSTRAINT pk_granja PRIMARY KEY ( id_granja )
 );

CREATE  TABLE "public".imagenes_pescados ( 
	id_img_pescado       serial  NOT NULL  ,
	nombre_archivo       varchar(255)  NOT NULL  ,
	ruta_archivo         varchar(500)  NOT NULL  ,
	especie              varchar(100)  NOT NULL  ,
	fecha_subida         timestamp DEFAULT CURRENT_TIMESTAMP   ,
	tamaño_archivo       bigint    ,
	ancho                integer    ,
	alto                 integer    ,
	formato              varchar(10)    ,
	etiquetas            jsonb    ,
	usuario_subio        varchar(100)    ,
	id_lote              integer    ,
	CONSTRAINT imagenes_pescados_pkey PRIMARY KEY ( id_img_pescado )
 );

CREATE  TABLE "public".estanque ( 
	id_estanque          integer    ,
	capacidad            integer    ,
	fecha_operacion      date    ,
	id_granja            integer    ,
	CONSTRAINT unq_estanque_id_estanque UNIQUE ( id_estanque ) 
 );

CREATE  TABLE "public".cosecha ( 
	id_lote              integer  NOT NULL  ,
	id_estanque          integer  NOT NULL  ,
	fecha_cosecha        date    ,
	CONSTRAINT pk_cosecha PRIMARY KEY ( id_lote, id_estanque )
 );

ALTER TABLE "public".ciudad ADD CONSTRAINT fk_ciudad_municipio FOREIGN KEY ( id_municipio ) REFERENCES "public".municipio( id_municipio );

ALTER TABLE "public".cosecha ADD CONSTRAINT fk_cosecha_estanque FOREIGN KEY ( id_estanque ) REFERENCES "public".estanque( id_estanque );

ALTER TABLE "public".cosecha ADD CONSTRAINT fk_cosecha_lotes FOREIGN KEY ( id_lote ) REFERENCES "public".lotes( id_lote );

ALTER TABLE "public".estanque ADD CONSTRAINT fk_estanque_granja FOREIGN KEY ( id_granja ) REFERENCES "public".granja( id_granja );

ALTER TABLE "public".granja ADD CONSTRAINT fk_granja_ciudad FOREIGN KEY ( id_ciudad ) REFERENCES "public".ciudad( id_ciudad );

ALTER TABLE "public".imagenes_pescados ADD CONSTRAINT fk_imagenes_pescados_lotes FOREIGN KEY ( id_lote ) REFERENCES "public".lotes( id_lote );

ALTER TABLE "public".lotes ADD CONSTRAINT fk_lotes_especies FOREIGN KEY ( id_especie ) REFERENCES "public".especies( id_especie );

ALTER TABLE "public".municipio ADD CONSTRAINT fk_municipio_departamento FOREIGN KEY ( id_departamento ) REFERENCES "public".departamento( id_departamento );

ALTER TABLE "public"."user" ADD CONSTRAINT fk_user_rol FOREIGN KEY ( id_rol ) REFERENCES "public".rol( id_rol );

