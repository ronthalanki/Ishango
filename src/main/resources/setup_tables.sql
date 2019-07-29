USE testdb;

DROP TABLE IF EXISTS Candidates, Users, Votes;

CREATE TABLE Candidates(Id BIGINT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(100));
CREATE TABLE Users(Id BIGINT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(100));
CREATE TABLE Votes(Id BIGINT PRIMARY KEY AUTO_INCREMENT, UserId BIGINT, FOREIGN KEY(UserId) REFERENCES Users(Id) 
    ON DELETE CASCADE, CandidateId BIGINT, Rank BIGINT, FOREIGN KEY(CandidateId) REFERENCES Candidates(Id) 
    ON DELETE CASCADE);


INSERT INTO Candidates(Id, Name) VALUES(1, 'Arthur Allen');
INSERT INTO Candidates(Id, Name) VALUES(2, 'Bob Brown');
INSERT INTO Candidates(Id, Name) VALUES(3, 'Carl Cook');

INSERT INTO Users(Id, Name) VALUES(1, 'David Davis');
INSERT INTO Users(Id, Name) VALUES(2, 'Ethan Evans');
INSERT INTO Users(Id, Name) VALUES(3, 'Frank Foster');
INSERT INTO Users(Id, Name) VALUES(4, 'Greg Griffin');
INSERT INTO Users(Id, Name) VALUES(5, 'Hank Hayes');

INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(1, 1, 1, 1);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(2, 1, 2, 2);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(3, 1, 3, 3);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(4, 2, 1, 1);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(5, 2, 2, 2);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(6, 2, 3, 3);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(7, 3, 1, 1);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(8, 3, 2, 2);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(9, 3, 3, 3);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(10, 4, 1, 1);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(11, 4, 2, 2);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(12, 4, 3, 3);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(13, 5, 1, 1);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(14, 5, 2, 2);
INSERT INTO Votes(Id, UserId, Rank, CandidateId) VALUES(15, 5, 3, 3);
