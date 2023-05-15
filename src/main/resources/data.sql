insert into genre (name) values ('Soul');
insert into genre (name) values ('Rock');
insert into genre (name) values ('Pop');
insert into genre (name) values ('Classic');
insert into genre (name) values ('Jazz');
insert into genre (name) values ('Hip-hop');
insert into genre (name) values ('Trash');
insert into genre (name) values ('Gregory');
insert into genre (name) values ('Children');
insert into genre (name) values ('Heavy Metal');
insert into genre (name) values ('New Age');

insert into track (title, duration_seconds) values ('La lluna la pruna', 45);
insert into track (title, duration_seconds) values ('El Joan Petit quan balla', 80);
insert into track (title, duration_seconds) values ('Back in black', 80);
insert into track (title, duration_seconds) values ('Sound of silence', 180);
insert into track (title, duration_seconds) values ('Vuela con el viento', 70);
insert into track (title, duration_seconds) values ('Lovesong from the mountains', 150);
insert into track (title, duration_seconds) values ('We will rock you', 150);
insert into track (title, duration_seconds) values ('From hell to heaven', 150);
insert into track (title, duration_seconds) values ('Tubular bells', 150);

insert into user_fy (name, second_name, email, password) values ('Josep', 'Alvarez', 'jalvarez@tecnocampus.cat', '{bcrypt}$2a$10$fVKfcc47q6lrNbeXangjYeY000dmjdjkdBxEOilqhapuTO5ZH0co2');
insert into user_fy (name, second_name, email, password) values ('Maria', 'Perez', 'mperez@tecnocampus.cat', '{bcrypt}$2a$10$fVKfcc47q6lrNbeXangjYeY000dmjdjkdBxEOilqhapuTO5ZH0co2');
insert into user_fy (name, second_name, email, password) values ('Josep', 'Alcobe', 'jalcobe@tecnocampus.cat', '{bcrypt}$2a$10$fVKfcc47q6lrNbeXangjYeY000dmjdjkdBxEOilqhapuTO5ZH0co2');
insert into user_fy (name, second_name, email, password) values ('un-authorized', 'Poor-gay', 'un-authorized@tecnocampus.cat', '{bcrypt}$2a$10$fVKfcc47q6lrNbeXangjYeY000dmjdjkdBxEOilqhapuTO5ZH0co2');

insert into role (name) values ('ROLE_FREE');
insert into role (name) values ('ROLE_PREMIUM');
insert into role (name) values ('ROLE_PROFESSIONAL');

insert into user_roles (user_id, role_id) values (1,1);
insert into user_roles (user_id, role_id) values (2,1);
insert into user_roles (user_id, role_id) values (3,3);

insert into track_genre (genre_id, track_id) values (9,1);
insert into track_genre (genre_id, track_id) values (9,2);
insert into track_genre (genre_id, track_id) values (3,1);
insert into track_genre (genre_id, track_id) values (3,2);
insert into track_genre (genre_id, track_id) values (4,2);
insert into track_genre (genre_id, track_id) values (10,3);
insert into track_genre (genre_id, track_id) values (11,4);
insert into track_genre (genre_id, track_id) values (11,5);
insert into track_genre (genre_id, track_id) values (11,6);

insert into track_artist (track_id, artist_id) values (1,1);
insert into track_artist (track_id, artist_id) values (1,2);
insert into track_artist (track_id, artist_id) values (2,1);
insert into track_artist (track_id, artist_id) values (2,2);
insert into track_artist (track_id, artist_id) values (3,3);
insert into track_artist (track_id, artist_id) values (4,3);
insert into track_artist (track_id, artist_id) values (5,3);
insert into track_artist (track_id, artist_id) values (6,3);
insert into track_artist (track_id, artist_id) values (7,3);
insert into track_artist (track_id, artist_id) values (8,3);
insert into track_artist (track_id, artist_id) values (9,3);


insert into play_list (title, description, open, owner_id) values ('The best', 'Best music ever', 1, 2);

insert into play_list_tracks (play_list_id, tracks_id) values (1, 1);
insert into play_list_tracks (play_list_id, tracks_id) values (1, 2);

insert into like_track (date, track_id, user_fy_id, liked ) values (current_timestamp(), 1, 2, 1);
insert into like_track (date, track_id, user_fy_id, liked ) values (current_timestamp(), 1, 1, 1);
insert into like_track (date, track_id, user_fy_id, liked ) values (current_timestamp(), 1, 3, 1);
insert into like_track (date, track_id, user_fy_id, liked ) values (current_timestamp(), 2, 2, 1);
insert into like_track (date, track_id, user_fy_id, liked ) values (current_timestamp(), 2, 1, 1);
insert into like_track (date, track_id, user_fy_id, liked ) values (current_timestamp(), 3, 1, 1);
insert into like_track (date, track_id, user_fy_id, liked ) values (current_timestamp(), 4, 1, 1);
insert into like_track (date, track_id, user_fy_id, liked ) values (current_timestamp(), 5, 1, 1);

insert into dj_list (title, description, owner_id) values ('The Maria DJ list', 'Selected chilly tracks', 3);

