INSERT INTO categories (name, description) VALUES
                                               ('Strength', 'Strength and resistance training exercises'),
                                               ('Cardio', 'Cardiovascular endurance exercises'),
                                               ('Flexibility', 'Stretching and flexibility exercises'),
                                               ('Balance', 'Balance and stability exercises'),
                                               ('Bodyweight', 'Exercises using body weight only');

INSERT INTO muscle_groups (name, description) VALUES
                                                  ('Chest', 'Pectoral muscles'),
                                                  ('Back', 'Latissimus dorsi and upper/lower back muscles'),
                                                  ('Shoulders', 'Deltoid muscles'),
                                                  ('Biceps', 'Front upper arm muscles'),
                                                  ('Triceps', 'Back upper arm muscles'),
                                                  ('Forearms', 'Lower arm muscles'),
                                                  ('Abs', 'Abdominal core muscles'),
                                                  ('Lower Back', 'Lumbar muscles'),
                                                  ('Glutes', 'Buttock muscles'),
                                                  ('Quadriceps', 'Front thigh muscles'),
                                                  ('Hamstrings', 'Back thigh muscles'),
                                                  ('Calves', 'Lower leg muscles'),
                                                  ('Neck', 'Neck muscles'),
                                                  ('Full Body', 'Multiple muscle groups');

INSERT INTO exercises (name, description, category_id, video_url) VALUES
                                                  ('Bench Press', 'Barbell chest press exercise', 1, 'https://example.com/bench-press'),
                                                  ('Squat', 'Barbell back squat exercise', 1, 'https://example.com/squat'),
                                                  ('Deadlift', 'Barbell deadlift exercise', 1, 'https://example.com/deadlift'),
                                                  ('Pull Up', 'Bodyweight back exercise', 5, 'https://example.com/pull-up'),
                                                  ('Push Up', 'Bodyweight chest exercise', 5, 'https://example.com/push-up'),
                                                  ('Running', 'Treadmill or outdoor running', 2, 'https://example.com/running'),
                                                  ('Plank', 'Core stability exercise', 4, 'https://example.com/plank'),
                                                  ('Jump Rope', 'Cardio jump rope exercise', 2, 'https://example.com/jump-rope'),
                                                  ('Shoulder Press', 'Dumbbell or barbell shoulder press', 1, 'https://example.com/shoulder-press'),
                                                  ('Lunges', 'Leg strength and balance exercise', 5, 'https://example.com/lunges');

INSERT INTO exercise_muscle_groups (exercise_id, muscle_group_id, target_type) VALUES
                                                -- Bench Press
                                                (1, 1, 'primary'), -- Chest
                                                (1, 3, 'secondary'), -- Shoulders
                                                (1, 5, 'secondary'), -- Triceps

                                                -- Squat
                                                (2, 10, 'primary'), -- Quadriceps
                                                (2, 11, 'secondary'), -- Hamstrings
                                                (2, 9, 'secondary'), -- Glutes

                                                -- Deadlift
                                                (3, 2, 'primary'), -- Back
                                                (3, 11, 'secondary'), -- Hamstrings
                                                (3, 9, 'secondary'), -- Glutes
                                                (3, 8, 'secondary'), -- Lower Back

                                                -- Pull Up
                                                (4, 2, 'primary'), -- Back
                                                (4, 4, 'secondary'), -- Biceps

                                                -- Push Up
                                                (5, 1, 'primary'), -- Chest
                                                (5, 3, 'secondary'), -- Shoulders
                                                (5, 5, 'secondary'), -- Triceps

                                                -- Running
                                                (6, 14, 'primary'), -- Full Body
                                                (6, 12, 'secondary'), -- Calves
                                                (6, 10, 'secondary'), -- Quadriceps

                                                -- Plank
                                                (7, 7, 'primary'), -- Abs
                                                (7, 8, 'secondary'), -- Lower Back

                                                -- Jump Rope
                                                (8, 12, 'primary'), -- Calves
                                                (8, 14, 'secondary'), -- Full Body

                                                -- Shoulder Press
                                                (9, 3, 'primary'), -- Shoulders
                                                (9, 5, 'secondary'), -- Triceps

                                                -- Lunges
                                                (10, 10, 'primary'), -- Quadriceps
                                                (10, 9, 'secondary'), -- Glutes
                                                (10, 11, 'secondary'); -- Hamstrings