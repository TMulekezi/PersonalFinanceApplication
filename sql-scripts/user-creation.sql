-- Drop user first if they exist
DROP USER if exists 'gfinanceuser'@'localhost';

-- Create user with privileges
CREATE USER 'gfinanceuser'@'localhost' IDENTIFIED BY '1501'; 
                                                      
GRANT ALL PRIVILEGES ON * . * TO 'gfinanceuser'@'localhost';
