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

INSERT INTO "public".departamento( id_departamento, nombre ) VALUES ( 1, 'Meta');
INSERT INTO "public".departamento( id_departamento, nombre ) VALUES ( 2, 'Huila');
INSERT INTO "public".departamento( id_departamento, nombre ) VALUES ( 3, 'Tolima');
INSERT INTO "public".departamento( id_departamento, nombre ) VALUES ( 4, 'Valle del Cauca');
INSERT INTO "public".departamento( id_departamento, nombre ) VALUES ( 5, 'Cundinamarca');
INSERT INTO "public".departamento( id_departamento, nombre ) VALUES ( 6, 'Antioquia');
INSERT INTO "public".departamento( id_departamento, nombre ) VALUES ( 7, 'Santander');
INSERT INTO "public".departamento( id_departamento, nombre ) VALUES ( 8, 'Caldas');
INSERT INTO "public".departamento( id_departamento, nombre ) VALUES ( 9, 'Risaralda');
INSERT INTO "public".departamento( id_departamento, nombre ) VALUES ( 10, 'Boyacá');
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 1, 'Villavicencio', 1);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 2, 'Granada', 1);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 3, 'Puerto López', 1);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 4, 'Puerto Gaitán', 1);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 5, 'Neiva', 2);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 6, 'Aipe', 2);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 7, 'Palermo', 2);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 8, 'Campoalegre', 2);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 9, 'Ibagué', 3);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 10, 'Espinal', 3);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 11, 'Purificación', 3);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 12, 'Melgar', 3);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 13, 'Cali', 4);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 14, 'Buga', 4);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 15, 'Palmira', 4);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 16, 'Jamundí', 4);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 17, 'Bogotá', 5);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 18, 'Girardot', 5);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 19, 'Facatativá', 5);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 20, 'Mosquera', 5);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 21, 'Medellín', 6);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 22, 'Rionegro', 6);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 23, 'Marinilla', 6);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 24, 'El Retiro', 6);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 25, 'Bucaramanga', 7);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 26, 'San Gil', 7);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 27, 'Barrancabermeja', 7);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 28, 'Puerto Wilches', 7);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 29, 'Manizales', 8);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 30, 'La Dorada', 8);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 31, 'Victoria', 8);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 32, 'Samaná', 8);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 33, 'Pereira', 9);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 34, 'Dosquebradas', 9);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 35, 'Santa Rosa de Cabal', 9);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 36, 'Tunja', 10);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 37, 'Puerto Boyacá', 10);
INSERT INTO "public".municipio( id_municipio, nombre, id_departamento ) VALUES ( 38, 'Aquitania', 10);
INSERT INTO "public".recomendacion( id_recomendacion, nombre, descripcion, url ) VALUES ( 1, 'Aplians Fish', 'Software de acuicultura que actúa en diferentes sectores de la piscicultura, como el cultivo de Tilapia, Tambaqui, Trucha y Camarón. Ofrece una solución integrada con conexión dual (online + offline) y aplicaciones móviles.', 'https://www.aplians.com/');
INSERT INTO "public".recomendacion( id_recomendacion, nombre, descripcion, url ) VALUES ( 2, 'AQUI9', 'Plataforma de monitoreo en tiempo real que utiliza sensores IoT para rastrear parámetros críticos de la calidad del agua como oxígeno disuelto, temperatura, pH y amoníaco. Emplea inteligencia artificial para analizar los datos, predecir tendencias y alertar sobre condiciones peligrosas antes de que afecten a los peces. Su objetivo es optimizar la salud de los peces y la eficiencia de la alimentación.', 'https://aqui9.com.br');
INSERT INTO "public".recomendacion( id_recomendacion, nombre, descripcion, url ) VALUES ( 3, 'Bluegrove', 'Combina sensores submarinos (Boyas Subguave) con una plataforma de software basada en IA llamada "Sensing as a Service". La tecnología monitoriza el comportamiento de los peces (apetito, estrés) y las condiciones ambientales, proporcionando insights para la toma de decisiones en la alimentación y la salud de los peces.', 'https://www.cageeye.com');
INSERT INTO "public".recomendacion( id_recomendacion, nombre, descripcion, url ) VALUES ( 4, 'Planports CRM+', 'Este software está diseñado específicamente para empresas de alimentación y agronegocios. Combina funciones de CRM (gestión de clientes, contactos, pedidos) con gestión de procesos y pre-contabilidad. Es ideal para piscifactorías que necesitan gestionar su cartera de clientes, logística de ventas y órdenes de pedido de manera integrada.', 'https://www.planports.com/es/crm');
INSERT INTO "public".recomendacion( id_recomendacion, nombre, descripcion, url ) VALUES ( 5, 'AgriERP', 'AgriERP es un sistema de planificación de recursos empresariales (ERP) para el sector agropecuario que incluye un módulo específico para acuicultura. Gestiona la producción, la cadena de frío, la trazabilidad y, de manera integrada, también ofrece funcionalidades de CRM para manejar las relaciones con los clientes que compran el producto final.', 'https://www.agrierp.com/');
INSERT INTO "public".recomendacion( id_recomendacion, nombre, descripcion, url ) VALUES ( 6, 'Odoo', 'Odoo es un ERP de código abierto y altamente personalizable. Una piscifactoría puede implementar sus módulos de CRM para gestionar leads y ventas, y el módulo de Manufactura para planificar y controlar el proceso productivo (CRP). Su flexibilidad permite adaptarlo a las necesidades específicas del negocio.', 'https://www.odoo.com/es_ES/app/crm');
INSERT INTO "public".recomendacion( id_recomendacion, nombre, descripcion, url ) VALUES ( 7, 'Eruvaka', 'Especializada en acuicultura, Eruvaka desarrolla dispositivos de monitoreo y control que se integran con una plataforma en la nube. Sus sistemas automatizan la oxigenación y controlan la alimentación en estanques, utilizando los datos recogidos para optimizar el rendimiento y alertar a los acuicultores en tiempo real.', 'https://eruvaka.com');
INSERT INTO "public".rol( rol, descripcion, permisos, id_rol ) VALUES ( 'Administrador', 'Superusuario', null, 1);
INSERT INTO "public".rol( rol, descripcion, permisos, id_rol ) VALUES ( 'Piscicultor', 'Usuario con permisos, básicos como ver registros, crear ya se lotes, granjas etc.', null, 2);
INSERT INTO "public".rol( rol, descripcion, permisos, id_rol ) VALUES ( 'Tecnico', 'Usuario con pocos permisos como subir los datos en el campo, ver reportes y registrar informacion sobre la alimentación.', null, 3);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Villavicencio', 1, 1);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Granada', 2, 2);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Puerto López', 3, 3);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Puerto Gaitán', 4, 4);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Neiva', 5, 5);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Aipe', 6, 6);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Palermo', 7, 7);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Campoalegre', 8, 8);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Ibagué', 9, 9);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Espinal', 10, 10);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Purificación', 11, 11);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Melgar', 12, 12);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Cali', 13, 13);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Buga', 14, 14);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Palmira', 15, 15);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Jamundí', 16, 16);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Bogotá', 17, 17);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Girardot', 18, 18);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Facatativá', 19, 19);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Mosquera', 20, 20);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Medellín', 21, 21);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Rionegro', 22, 22);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Marinilla', 23, 23);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'El Retiro', 24, 24);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Bucaramanga', 25, 25);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'San Gil', 26, 26);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Barrancabermeja', 27, 27);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Puerto Wilches', 28, 28);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Manizales', 29, 29);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'La Dorada', 30, 30);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Victoria', 31, 31);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Samaná', 32, 32);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Pereira', 33, 33);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Dosquebradas', 34, 34);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Santa Rosa de Cabal', 35, 35);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Tunja', 36, 36);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Puerto Boyacá', 37, 37);
INSERT INTO "public".ciudad( nombre, id_ciudad, id_municipio ) VALUES ( 'Aquitania', 38, 38);
