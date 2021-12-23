USE algohub;

CREATE TABLE classification (
   id VARCHAR(36) NOT NULL,
   name VARCHAR(100) NOT NULL,
   parent_id VARCHAR(36) NULL,
   author_id VARCHAR(36) NOT NULL
);

ALTER TABLE classification ADD CONSTRAINT classification_pk PRIMARY KEY (id);
ALTER TABLE classification ADD CONSTRAINT classification_parent_id_fk FOREIGN KEY (parent_id) REFERENCES classification (id) ON DELETE CASCADE;

CREATE TABLE algorithm (
   id VARCHAR(36) NOT NULL,
   name VARCHAR(100) NOT NULL,
   description VARCHAR(255) NOT NULL,
   classification_id VARCHAR(36) NOT NULL,
   author_id VARCHAR(36) NOT NULL
);

ALTER TABLE algorithm ADD CONSTRAINT algorithm_pk PRIMARY KEY (id);
ALTER TABLE algorithm ADD CONSTRAINT algorithm_classification_id_fk FOREIGN KEY (classification_id) REFERENCES classification (id) ON DELETE CASCADE;

CREATE TABLE implementation (
   id VARCHAR(36) NOT NULL,
   name VARCHAR(100) NOT NULL,
   filename VARCHAR(255) NOT NULL,
   algorithm_id VARCHAR(36) NOT NULL,
   author_id VARCHAR(36) NOT NULL
);

ALTER TABLE implementation ADD CONSTRAINT implementation_pk PRIMARY KEY (id);
ALTER TABLE implementation ADD CONSTRAINT implementation_algorithm_id_fk FOREIGN KEY (algorithm_id) REFERENCES algorithm (id) ON DELETE CASCADE;

CREATE TABLE problem_instance (
   id VARCHAR(36) NOT NULL,
   dataset_filename VARCHAR(100) NOT NULL,
   dataset_size INT NOT NULL,
   problem_type VARCHAR(100) NOT NULL,
   algorithm_id VARCHAR(36) NOT NULL,
   author_id VARCHAR(36) NOT NULL
);

ALTER TABLE problem_instance ADD CONSTRAINT problem_instance_pk PRIMARY KEY (id);
ALTER TABLE problem_instance ADD CONSTRAINT problem_instance_algorithm_id_fk FOREIGN KEY (algorithm_id) REFERENCES algorithm (id) ON DELETE CASCADE;

CREATE TABLE benchmark (
   id VARCHAR(36) NOT NULL,
   memory INT NOT NULL,
   cpu_name VARCHAR(100) NOT NULL,
   cpu_threads INT NOT NULL,
   cpu_cores INT NOT NULL,
   cpu_l1_cache INT NOT NULL,
   cpu_l2_cache INT NOT NULL,
   cpu_l3_cache INT NOT NULL,
   execution_date DATE NOT NULL,
   execution_time DOUBLE NOT NULL,
   memory_usage DOUBLE NOT NULL,
   implementation_id VARCHAR(36) NOT NULL,
   problem_instance_id VARCHAR(36) NOT NULL
);

ALTER TABLE benchmark ADD CONSTRAINT benchmark_pk PRIMARY KEY (id);
ALTER TABLE benchmark ADD CONSTRAINT benchmark_implementation_id_fk FOREIGN KEY (implementation_id) REFERENCES implementation (id) ON DELETE CASCADE;
ALTER TABLE benchmark ADD CONSTRAINT benchmark_problem_instance_id_fk FOREIGN KEY (problem_instance_id) REFERENCES problem_instance (id) ON DELETE CASCADE;