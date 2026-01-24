-- ================================================
-- WORKOUT TRACKER DATABASE SCHEMA
-- ================================================


-- ================================================
-- TABLE: users
-- ================================================
CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       full_name VARCHAR(255) NOT NULL,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       INDEX idx_username (username)
);

-- ================================================
-- TABLE: categories
-- ================================================
CREATE TABLE categories (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(100) UNIQUE NOT NULL,
                            description TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================================
-- TABLE: muscle_groups
-- ================================================
CREATE TABLE muscle_groups (
                               id INT PRIMARY KEY AUTO_INCREMENT,
                               name VARCHAR(100) UNIQUE NOT NULL,
                               description TEXT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================================
-- TABLE: exercises
-- ================================================
CREATE TABLE exercises (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(255) UNIQUE NOT NULL,
                           description TEXT,
                           category_id INT NOT NULL,
                           video_url VARCHAR(500),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
                           INDEX idx_category (category_id),
                           INDEX idx_name (name)
);

-- ================================================
-- TABLE: exercise_muscle_groups (Many-to-Many)
-- ================================================
CREATE TABLE exercise_muscle_groups (
                                        id INT PRIMARY KEY AUTO_INCREMENT,
                                        exercise_id INT NOT NULL,
                                        muscle_group_id INT NOT NULL,
                                        target_type ENUM('primary', 'secondary') NOT NULL DEFAULT 'primary',
                                        FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE CASCADE,
                                        FOREIGN KEY (muscle_group_id) REFERENCES muscle_groups(id) ON DELETE CASCADE,
                                        UNIQUE KEY unique_exercise_muscle (exercise_id, muscle_group_id),
                                        INDEX idx_exercise (exercise_id),
                                        INDEX idx_muscle_group (muscle_group_id)
);

-- ================================================
-- TABLE: workout_plans
-- ================================================
CREATE TABLE workout_plans (
                               id INT PRIMARY KEY AUTO_INCREMENT,
                               user_id INT NOT NULL,
                               name VARCHAR(255) NOT NULL,
                               description TEXT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               INDEX idx_user (user_id)
);

-- ================================================
-- TABLE: workout_sessions
-- ================================================
CREATE TABLE workout_sessions (
                                  id INT PRIMARY KEY AUTO_INCREMENT,
                                  workout_plan_id INT NOT NULL,
                                  name VARCHAR(255) NOT NULL,
                                  scheduled_date DATETIME,
                                  notes TEXT,
                                  status ENUM('pending', 'completed', 'skipped') NOT NULL DEFAULT 'pending',
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  FOREIGN KEY (workout_plan_id) REFERENCES workout_plans(id) ON DELETE CASCADE,
                                  INDEX idx_workout_plan (workout_plan_id),
                                  INDEX idx_scheduled_date (scheduled_date),
                                  INDEX idx_status (status)
);

-- ================================================
-- TABLE: session_exercises
-- ================================================
CREATE TABLE session_exercises (
                                   id INT PRIMARY KEY AUTO_INCREMENT,
                                   workout_session_id INT NOT NULL,
                                   exercise_id INT NOT NULL,
                                   sets INT,
                                   reps INT,
                                   weight DECIMAL(6, 2),
                                   duration_minutes INT COMMENT 'For cardio exercises',
                                   order_index INT NOT NULL DEFAULT 0,
                                   notes TEXT,
                                   completed BOOLEAN DEFAULT FALSE,
                                   FOREIGN KEY (workout_session_id) REFERENCES workout_sessions(id) ON DELETE CASCADE,
                                   FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE RESTRICT,
                                   INDEX idx_workout_session (workout_session_id),
                                   INDEX idx_exercise (exercise_id),
                                   INDEX idx_order (order_index)
);