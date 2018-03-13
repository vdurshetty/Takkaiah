
drop table CustItemMapping CASCADE;
drop table CGArticleCodes CASCADE;
drop table CustomerMaster CASCADE; 
drop table ItemMRP CASCADE; 
drop table ItemMaster CASCADE; 
drop table ItemCategoryMaster CASCADE;  
drop table CustomerGroup  CASCADE; 
drop table MRPType  CASCADE;  
drop table PORA_Functionalities CASCADE;
drop table userPermissions CASCADE;
drop table Users CASCADE;

Create table ItemCategoryMaster (
    CatID serial primary key,
    CatName varchar(200) unique, 
	CatAlias varchar(200)
);

Create table MRPType (  
    mrpTID serial primary key,
    mrpTName varchar(200) unique not null,
    mrpTAlias varchar(100),
	mrpTaxPercent decimal(8,2) ,
    remarks varchar(400) 
 );


Create table ItemMaster (
    ItemID serial primary key,
    EANCode varchar(100) unique,
    ItemName Varchar(400) unique,
    Alias Varchar(400),
    CatID int references ItemCategoryMaster(catID),
    Units varchar(100),
	TaxPercent decimal(8,2),
	CaseQty int
 );

select * from ItemMaster;

Create table ItemMRP (
    imprID serial primary key,
    ItemID int references ItemMaster(ItemID),
    mrpType int references MRPType(mrpTID),
    mrp numeric(8,2)
);

Create table CustomerGroup (
    CustGrpID serial primary key,
    CustGrpName varchar(200) unique,
    CustGrpAlias varchar(200),
    Address varchar(1000),
    remarks varchar(1000)
);

Create table CustomerMaster (
    CustID serial primary key,
    CustName Varchar(400) unique not null,
    CustGroup int references CustomerGroup(CustGrpID),
    mrpType int references MRPType(mrpTID),
    Address varchar(400),
    EmailID varchar(200),
    Mobile varchar(20),
    Notes varchar(2000),
    Remarks varchar(1000)
);

create table CGArticleCodes(
	cgAID serial Primary key,
	cgID int references CustomerGroup(CustGrpID),
	itemID int references ItemMaster(itemID),
	ArticleCode varchar(200),
	CONSTRAINT CGACodesUniqueContraint UNIQUE (cgID,ItemID,ArticleCode)
);

Create table CustItemMapping (
    CimID serial primary key,
    CustID int references CustomerMaster(CustID) not null,
    ItemID int references ItemMaster(ItemID) not null,
    MarginPercent decimal(8,2),
	CONSTRAINT custItemUniqueContraint UNIQUE (CustID,ItemID)
);


Create table Users (
    uid serial primary key,
    UserName varchar(200) unique,
	FullName varchar(200),
	empid  varchar(100),
    pwd varchar(500)
);

create table PORA_Functionalities(
    fid serial primary key,
	fName   varchar(200) unique ,
	description varchar(1000)
);

Create table userPermissions(
	uid int references Users(uid) not null,
	fid int references PORA_Functionalities(fid)
);

drop view OutstationCustMapItemsView CASCADE;
drop view LocalCustMapItemsView CASCADE;
drop view CustItemMapWithArticleCodeView CASCADE;
drop view CustItemMapView CASCADE;
drop view CustArticleCodeView CASCADE;


CREATE OR REPLACE VIEW CustArticleCodeView as 
SELECT b.cgAID,a.itemID,a.itemName,a.hsn_code,a.eanCode,b.articleCode,b.cgid FROM ItemMaster AS a LEFT JOIN CGArticleCodes AS b ON b.itemId = a.itemID;


CREATE OR REPLACE VIEW CustItemMapView as 
select a.cimid,a.MarginPercent,b.itemid,b.itemname,b.hsn_code,b.eancode,b.TaxPercent,b.caseqty,c.custgroup,d.CustGrpName,c.custid,c.CustName,c.mrptype 
from custitemmapping as a,itemMaster b, customermaster c , CustomerGroup d
where b.itemid = a.itemid and c.custid=a.custid and d.CustGrpID=c.custgroup;

CREATE OR REPLACE VIEW CustItemMapWithArticleCodeView as 
select a.cimid,a.itemid,a.itemname,a.hsn_code,a.eancode,a.TaxPercent,a.caseqty,a.custgroup,a.custid,a.CustName,a.CustGrpName,a.mrptype,a.MarginPercent,b.articlecode 
from CustItemMapView A LEFT JOIN CGArticleCodes AS b ON (b.itemId = a.itemID AND b.cgid=a.custgroup);

CREATE OR REPLACE VIEW LocalCustMapItemsView as 
select a.cimid,a.itemid,a.itemname,a.hsn_code,a.eancode,a.articlecode,a.TaxPercent,a.caseqty,a.custgroup,a.custid,a.CustName,a.mrptype,a.MarginPercent,b.mrp,
round((b.mrp- (b.mrp * (a.MarginPercent/100)))::numeric,2) as NetLandingPrice ,
round(( (b.mrp- (b.mrp * (a.MarginPercent/100))) * (100/(100+a.TaxPercent)))::numeric,2) as BasicCostPrice, 
round((( (b.mrp- (b.mrp * (a.MarginPercent/100))) * (100/(100+ a.TaxPercent)))*( a.TaxPercent/100))::numeric,2) as TaxVal 
from CustItemMapWithArticleCodeView as A LEFT JOIN ItemMRP as B on (b.itemid=a.itemid and b.mrptype=a.mrptype);

CREATE OR REPLACE VIEW OutstationCustMapItemsView as 
select a.cimid,a.itemid,a.itemname,a.hsn_code,a.eancode,a.articlecode,b.mrpTaxPercent as TaxPercent,a.caseqty,a.custid,a.CustName,a.MarginPercent,a.mrp,a.NetLandingPrice,
round(( NetLandingPrice * (100/(100+b.mrpTaxPercent)))::numeric,2) as BasicCostPrice, 
round((( NetLandingPrice * (100/(100+ b.mrpTaxPercent)))*( b.mrpTaxPercent/100))::numeric,2) as TaxVal
from LocalCustMapItemsView a, MRPType b where a.mrptype=b.mrpTID; 




-- delete from PORA_Functionalities;

insert into PORA_Functionalities (fid,fName,description) values (1,'MRP Type Form','Access for MRP Type Form');
insert into PORA_Functionalities (fid,fName,description) values (2,'Item Category Form','Access for Item Category Form');
insert into PORA_Functionalities (fid,fName,description) values (3,'Customer Group Form','Access for Customer Group Form');
insert into PORA_Functionalities (fid,fName,description) values (4,'Customer Master Form','Access for Customer Form');
insert into PORA_Functionalities (fid,fName,description) values (5,'Item Master Form','Access for Item Master');
insert into PORA_Functionalities (fid,fName,description) values (6,'Customer Article Codes Form','Access for Customer Article Codes Form');
insert into PORA_Functionalities (fid,fName,description) values (7,'Customer Item Mapping Form','Access for Customer Item Mapping');
insert into PORA_Functionalities (fid,fName,description) values (8,'User Permissions Form','Access Grant/Revoke Form');
insert into PORA_Functionalities (fid,fName,description) values (9,'User Management Form','Access for User Form');
insert into PORA_Functionalities (fid,fName,description) values (10,'Password Change Form','Access Grant/Revoke Form');
insert into PORA_Functionalities (fid,fName,description) values (11,'PO Reader Form','Access for PO Reader Form');
insert into PORA_Functionalities (fid,fName,description) values (12,'User List Report','User List Report');
insert into PORA_Functionalities (fid,fName,description) values (13,'User Permissions Report','User Permissions Report');
insert into PORA_Functionalities (fid,fName,description) values (14,'MRP Type Report','MRP Type Report');
insert into PORA_Functionalities (fid,fName,description) values (15,'Item Category Report','Item Category Report');
insert into PORA_Functionalities (fid,fName,description) values (16,'Customer Group Report','Customer Group Report');
insert into PORA_Functionalities (fid,fName,description) values (17,'Item Master Report','Item Master Report');
insert into PORA_Functionalities (fid,fName,description) values (18,'Item Price Report','Item Price Report');
insert into PORA_Functionalities (fid,fName,description) values (19,'Customer Master Report','Customer Master Report');
insert into PORA_Functionalities (fid,fName,description) values (20,'Customer Article Codes Report','Customer Article Codes Report');
insert into PORA_Functionalities (fid,fName,description) values (21,'Customer Item Mapping Report','Access for Customer Items Report');
insert into PORA_Functionalities (fid,fName,description) values (22,'Check PO Details Form','Access for Check PO Details Form');


insert into users (UserName,FullName,pwd) values ('Admin','Admistrator','VTlUc2c5r2FuogI9IjASdQ==');
-- delete from userPermissions;

insert into userPermissions (uid,fid) values (1,1);
insert into userPermissions (uid,fid) values(1,2);
insert into userPermissions (uid,fid) values(1,3);
insert into userPermissions (uid,fid) values(1,4);
insert into userPermissions (uid,fid) values(1,5);
insert into userPermissions (uid,fid) values(1,6);
insert into userPermissions (uid,fid) values(1,7);
insert into userPermissions (uid,fid) values(1,8);
insert into userPermissions (uid,fid) values(1,9);
insert into userPermissions (uid,fid) values(1,10);
insert into userPermissions (uid,fid) values(1,11);
insert into userPermissions (uid,fid) values(1,12);
insert into userPermissions (uid,fid) values(1,13);
insert into userPermissions (uid,fid) values(1,14);
insert into userPermissions (uid,fid) values(1,15);
insert into userPermissions (uid,fid) values(1,16);
insert into userPermissions (uid,fid) values(1,17);
insert into userPermissions (uid,fid) values(1,18);
insert into userPermissions (uid,fid) values(1,19);
insert into userPermissions (uid,fid) values(1,20);
insert into userPermissions (uid,fid) values(1,21);
insert into userPermissions (uid,fid) values(1,22);


insert into MRPType (mrpTName,mrpTAlias,mrpTaxPercent) values ('Local MRP','Local',0);
insert into MRPType (mrpTName,mrpTAlias,mrpTaxPercent) values ('Out Station MRP','Out Station',2.0);

insert into ItemCategoryMaster (CatName,CatAlias) values ('Dry Fruits','Dry Fruits');
insert into ItemCategoryMaster (CatName,CatAlias) values ('Species','Species');

insert into customergroup (custgrpname,custgrpalias) values ('Reliance India Limited','Reliance');
insert into customergroup (custgrpname,custgrpalias) values ('Spencers','Spencers');
insert into customergroup (custgrpname,custgrpalias) values ('HyperCity','HyperCity');
insert into customergroup (custgrpname,custgrpalias) values ('Aditya Birla','Aditya Birla');
insert into customergroup (custgrpname,custgrpalias) values ('Max Hyper Market','Max Hyper Market');
insert into customergroup (custgrpname,custgrpalias) values ('Big Bazar','Big Bazar');

/*
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640693','DELICIOUS PHOOL MAKHANA 50G','Phool Makhana 50 G',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640648','DELICIOUS KHUS KHUS 100G','KHUS KHUS',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640617','DELICIOUS KARAN PHOOL 20G','KARAN PHOOL',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640563','DELICIOUS CLOVES 20G','CLOVES',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640044','DELICIOUS BADAM ORD 100 GM PP','BADAM ORD',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640761','DELICIOUS MUSTARD BIG 100G','MUSTARD BIG',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640242','DELICIOUS DRY DATES 100 g','DRY DATES',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016641119','DELICIOUS BADAM ECO 100 g','BADAM ECO',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016641102','DELICIOUS HING POWDER 50G','HING POWDER',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640785','DELICIOUS TILL WHITE 100G','TILL WHITE',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640334','DELICIOUS KISMIS YELLOW100 GM PP','KISMIS YELLOW',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640068','DELICIOUS BADAM SELECTED 100 GM PP','BADAM SELECTED',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640822','DELICIOUS KISMIS YELLOW200 g','KISMIS YELLOW',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640198','DELICIOUS CASHEW WHL STD (320) 200 g','CASHEW WHL STD',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640143','DELICIOUS CASHEW SPLIT 200 g','CASHEW SPLIT',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640167','DELICIOUS CASHEW WHL RE g (240 )200 g','CASHEW WHL RE g (240)',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640037','DELICIOUS APRICOT SELECTED100 g','APRICOT SELECTED',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640389','DELICIOUS SALTED BADAM100 GM PP','SALTED BADAM',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640105','DELICIOUS CASHEW BROKEN 100 g PP','CASHEW BROKEN',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640181','DELICIOUS SWEET SAUNF 100G','SWEET SAUNF',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640136','DELICIOUS CASHEW SPLIT100 GM PP','CASHEW SPLIT',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640754','DELICIOUS DHANIA (CORIANDER) 200G','DHANIA (CORIANDER)',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016641126','DELICIOUS BADAM ECO 200 g','BADAM ECO',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640594','DELICIOUS JEERA 100G','JEERA',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640532','DELICIOUS CARDAMOM GREEN 20G','CARDAMOM GREEN',2,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640747','DELICIOUS SAUNF TABLE 100G','SAUNF TABLE',1,'EA',2);
insert into ItemMaster (eancode,itemname,alias,catid,units,taxpercent) values ('8906016640709','DELICIOUS SAUNF COOKING 100G','SAUNF COOKING',1,'EA',2);

insert into itemmrp (itemid,mrptype,mrp) values (19,2,130.00);
insert into itemmrp (itemid,mrptype,mrp) values (19,1,125.00);
insert into itemmrp (itemid,mrptype,mrp) values (21,1,142.00);
insert into itemmrp (itemid,mrptype,mrp) values (21,2,145.00);
insert into itemmrp (itemid,mrptype,mrp) values (17,1,30.00);
insert into itemmrp (itemid,mrptype,mrp) values (17,2,34.00);
insert into itemmrp (itemid,mrptype,mrp) values (34,1,152.00);
insert into itemmrp (itemid,mrptype,mrp) values (33,1,645.00);
insert into itemmrp (itemid,mrptype,mrp) values (5,1,184.00);
insert into itemmrp (itemid,mrptype,mrp) values (27,1,62.00);
insert into itemmrp (itemid,mrptype,mrp) values (26,1,55.00);
insert into itemmrp (itemid,mrptype,mrp) values (30,1,528.00);
insert into itemmrp (itemid,mrptype,mrp) values (29,1,191.00);
insert into itemmrp (itemid,mrptype,mrp) values (35,1,74.00);
insert into itemmrp (itemid,mrptype,mrp) values (48,1,330.00);
insert into itemmrp (itemid,mrptype,mrp) values (49,1,54.00);
insert into itemmrp (itemid,mrptype,mrp) values (6,1,26.00);
insert into itemmrp (itemid,mrptype,mrp) values (50,1,65.00);
insert into itemmrp (itemid,mrptype,mrp) values (32,1,135.00);
insert into itemmrp (itemid,mrptype,mrp) values (10,1,38.00);
insert into itemmrp (itemid,mrptype,mrp) values (14,1,302.00);
insert into itemmrp (itemid,mrptype,mrp) values (7,1,68.00);
insert into itemmrp (itemid,mrptype,mrp) values (24,1,67.00);
insert into itemmrp (itemid,mrptype,mrp) values (47,1,133.00);
insert into itemmrp (itemid,mrptype,mrp) values (1,1,79.00);
insert into itemmrp (itemid,mrptype,mrp) values (36,1,87.00);
insert into itemmrp (itemid,mrptype,mrp) values (37,1,122.00);
insert into itemmrp (itemid,mrptype,mrp) values (38,1,70.00);
insert into itemmrp (itemid,mrptype,mrp) values (39,1,92.00);
insert into itemmrp (itemid,mrptype,mrp) values (20,1,161.00);
insert into itemmrp (itemid,mrptype,mrp) values (20,2,145.00);
insert into itemmrp (itemid,mrptype,mrp) values (41,1,368.00);
insert into itemmrp (itemid,mrptype,mrp) values (43,1,425.00);
insert into itemmrp (itemid,mrptype,mrp) values (45,1,168.00);
insert into itemmrp (itemid,mrptype,mrp) values (46,1,77.00);
insert into itemmrp (itemid,mrptype,mrp) values (22,1,89.00);
insert into itemmrp (itemid,mrptype,mrp) values (11,1,86.00);
insert into itemmrp (itemid,mrptype,mrp) values (51,1,139.00);
insert into itemmrp (itemid,mrptype,mrp) values (52,1,129.00);
insert into itemmrp (itemid,mrptype,mrp) values (53,1,77.00);
insert into itemmrp (itemid,mrptype,mrp) values (2,1,106.00);
insert into itemmrp (itemid,mrptype,mrp) values (54,1,33.00);
insert into itemmrp (itemid,mrptype,mrp) values (55,1,27.00);
insert into itemmrp (itemid,mrptype,mrp) values (28,1,83.00);
*/

-- COPY ItemMaster TO 'D:/Venu/samples/TableData/itemmaster.csv' DELIMITER ',' CSV HEADER;
-- COPY ItemMRP TO 'D:/Venu/samples/TableData/ItemMRP.csv' DELIMITER ',' CSV HEADER;

-- COPY ItemMaster FROM 'D:/Venu/samples/TableData/itemmaster.csv' DELIMITER ',' CSV HEADER;
-- COPY ItemMRP FROM 'D:/Venu/samples/TableData/ItemMRP.csv' DELIMITER ',' CSV HEADER;

--ALTER TABLE tablename ADD CONSTRAINT CGACodesUniqueContraint UNIQUE (cgID,ItemID,ArticleCode);




------------------------------------------------------------
New Change - 03-Aug-2017
------------------------------------------------------------
ALTER TABLE ItemMaster  ADD COLUMN hsn_code varchar(12);
