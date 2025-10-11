INSERT INTO users (user_id, firstname, lastname, username, password, is_active)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Alice', 'Johnson', 'alicej', 'password1', true),
    ('22222222-2222-2222-2222-222222222222', 'Bob', 'Smith', 'bobsmith', 'password2', true),
    ('33333333-3333-3333-3333-333333333333', 'Charlie', 'Brown', 'charlieb', 'password3', true);


INSERT INTO trainee (trainee_id, user_id, date_of_birth, address)
VALUES
    ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', '2000-05-12', '123 Main St, City A');

INSERT INTO trainer (trainer_id, user_id, specialization)
VALUES
    ('bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', 'Fitness'),
    ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '33333333-3333-3333-3333-333333333333', 'Yoga');

INSERT INTO trainee_trainer (trainee_id, trainer_id)
VALUES
    ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
    ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb');

INSERT INTO training_type (training_type_id, training_type_name)
VALUES
    ('ccccccc1-cccc-cccc-cccc-cccccccccccc', 'Strength'),
    ('ccccccc2-cccc-cccc-cccc-cccccccccccc', 'Cardio'),
    ('ccccccc3-cccc-cccc-cccc-cccccccccccc', 'Yoga');

INSERT INTO training (training_id, trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration)
VALUES
    ('ddddddd1-dddd-dddd-dddd-dddddddddddd', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Morning Strength Session', 'ccccccc1-cccc-cccc-cccc-cccccccccccc', '2025-01-15', 60),
    ('ddddddd2-dddd-dddd-dddd-dddddddddddd', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Evening Yoga Class', 'ccccccc3-cccc-cccc-cccc-cccccccccccc', '2025-01-16', 45),
    ('ddddddd3-dddd-dddd-dddd-dddddddddddd', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Cardio Blast', 'ccccccc2-cccc-cccc-cccc-cccccccccccc', '2025-01-18', 30);
