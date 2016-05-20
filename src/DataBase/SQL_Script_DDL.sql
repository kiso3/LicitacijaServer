

CREATE TABLE [dbo].[Oglas](
	[OglasId]			INT				IDENTITY(1,1) NOT NULL,
	[ZaGodinu]			DATE			NOT NULL,
	[LicitatorskiKorak]	DECIMAL(12,2)	NOT NULL,
	[NazivOpstine]		NVARCHAR(100)	NOT NULL,
	[MaxPovrsinaZakupa]	DECIMAL(12,2)	NOT NULL,
	CONSTRAINT [PK_Oglas] PRIMARY KEY CLUSTERED ([OglasId]) 
)

CREATE TABLE [dbo].[Nadmetanje](
	[OglasId]			INT				NOT NULL,
	[Rb]				INT				NOT NULL,
	[Oznaka]			NVARCHAR(50)	NOT NULL,
	[PocetnaCena]		DECIMAL(12,2)	NOT NULL,
	[Depozit]			DECIMAL(12,2)	NOT NULL,
	[Povrsina]			DECIMAL(12,2)	NOT NULL,
	CONSTRAINT [PK_Nadmetanje] PRIMARY KEY CLUSTERED ([OglasId], [Rb]),
	CONSTRAINT [FK_Nadmetanje_Oglas] FOREIGN KEY ([OglasId])  REFERENCES [dbo].[Oglas] ([OglasId])
	ON DELETE CASCADE ON UPDATE CASCADE
)

CREATE TABLE [dbo].[Mesto](
	[MestoId]			INT				IDENTITY(1,1) NOT NULL,
	[Naziv]				NVARCHAR(100)	NOT NULL
	CONSTRAINT [PK_Mesto] PRIMARY KEY CLUSTERED ([MestoId])
)

CREATE TABLE [dbo].[Ucesnik](
	[UcesnikId]			INT				IDENTITY(1,1) NOT NULL,
	[Email]				NVARCHAR(100)	NULL,
	[Telegon]			NVARCHAR(50)	NULL,
	[Adresa]			NVARCHAR(100)	NULL,
	[MestoId]			INT				NOT NULL,
	CONSTRAINT [PK_Ucesnik] PRIMARY KEY CLUSTERED ([UcesnikID]),
	CONSTRAINT [FK_Ucesnik_Mesto] FOREIGN KEY ([MestoId]) REFERENCES [dbo].[Mesto] (MestoId)
	
)	

CREATE TABLE [dbo].[UcesnikFizicko](
	[UcesnikId]			INT				NOT NULL,
	[Jmbg]				CHAR(13)		NOT NULL,
	[Ime]				NVARCHAR(50)	NOT NULL,
	[ImeRoditelja]		NVARCHAR(50)	NULL,
	[Prezime]			NVARCHAR(50)	NOT NULL,
	CONSTRAINT [PK_UcesnikFizicko] PRIMARY KEY CLUSTERED ([UcesnikId]),
	CONSTRAINT [FK_UcesnikFizicko_Ucesnik] FOREIGN KEY ([UcesnikId]) REFERENCES [dbo].[Ucesnik] ([UcesnikId])
	ON DELETE CASCADE ON UPDATE CASCADE
)

CREATE TABLE [dbo].[UcesnikPravno](
	[UcesnikId]			INT				NOT NULL,
	[Mb]				CHAR(10)		NOT NULL,
	[Naziv]				VARCHAR(100)	NOT NULL,
	[Pib]				CHAR(10)		NOT NULL,
	CONSTRAINT [PK_UcesnikPravno] PRIMARY KEY CLUSTERED ([UcesnikId]),
	CONSTRAINT [FK_UcesnikPravno_Pravno] FOREIGN KEY ([UcesnikId]) REFERENCES [dbo].[Ucesnik] ([UcesnikId])
	ON DELETE CASCADE ON UPDATE CASCADE
)

CREATE TABLE [dbo].[Prijava](
	[PrijavaId]			INT				IDENTITY(1,1) NOT NULL,
	[DatumPodnosenja]	DATE			NOT NULL,
	[UpoznatSaPonudom]	BIT				NULL,
	[DatumObilaska]		DATE			NULL,
	[DokazoUplatiDepozita]BIT			NULL,
	[DokazoVlasnistvu]	BIT				NULL,
	[RegPoljGazdinstva]	BIT				NULL,
	[MestoId]			INT				NOT NULL,
	[OglasId]			INT				NOT NULL,
	[Rb]				INT				NOT NULL,
	CONSTRAINT [PK_Prijava] PRIMARY KEY CLUSTERED ([PrijavaId]),
	CONSTRAINT [FK_Prijava_Mesto] FOREIGN KEY ([MestoId]) REFERENCES [dbo].[Mesto] ([MestoId]),
	CONSTRAINT [FK_Prijava_Nadmetanje] FOREIGN KEY ([OglasId],[Rb]) REFERENCES [dbo].[Nadmetanje] ([OglasId],[Rb])
)

CREATE TABLE [dbo].[PrijavaFizicko](
	[PrijavaId]			INT				NOT NULL,
	[LicnaKarta]		BIT				NOT NULL,
	[DoazoGranicnomZemljistu]	BIT		NOT NULL,
	[UcesnikId]			INT				NOT NULL,
	CONSTRAINT [PK_PrijavaFizicko] PRIMARY KEY CLUSTERED ([PrijavaId]),
	CONSTRAINT [FK_PrijavaFizicko_Prijava] FOREIGN KEY ([PrijavaId]) REFERENCES [dbo].[Prijava] ([PrijavaId])
	ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT [FK_PrijavaFizicko_UcesnikFizicko] FOREIGN KEY ([UcesnikId]) REFERENCES [dbo].[UcesnikFizicko] ([UcesnikId])
	ON DELETE CASCADE ON UPDATE CASCADE
)

CREATE TABLE [dbo].[PrijavaPravno](
	[PrijavaId]			INT				NOT NULL,
	[IzvodIzPrivrednogRegistra]	INT		NOT NULL,
	[UcesnikId]			INT				NOT NULL,
	CONSTRAINT [PK_PrijavaPravno] PRIMARY KEY CLUSTERED ([PrijavaId]),
	CONSTRAINT [FK_PrijavaPravno_Prijava] FOREIGN KEY ([PrijavaId]) REFERENCES [dbo].[Prijava] ([PrijavaId])
	ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT [FK_PrijavaPravno_UcesnikPravno] FOREIGN KEY ([UcesnikId]) REFERENCES [dbo].[UcesnikPravno] ([UcesnikId])
	ON DELETE CASCADE ON UPDATE CASCADE
)

CREATE TABLE [dbo].[ZapisnikLicitacije](
	[ZapisnikLicitacijeId]	INT			IDENTITY(1,1) NOT NULL,
	[DatumVremePocetka]		DATETIME	NULL,
	[DatumVremeZavrsetka]	DATETIME	NULL,
	[AdresaLicitacije]		NVARCHAR(100)	NULL,
	[MestoId]				INT			NULL,
	[OglasId]				INT			NOT NULL,
	[Rb]					INT			NOT NULL,
	CONSTRAINT [PK_ZapsinikLicitacije] PRIMARY KEY CLUSTERED ([ZapisnikLicitacijeId]),
	CONSTRAINT [FK_ZapisnikLicitacije_Mesto] FOREIGN KEY ([MestoId]) REFERENCES [dbo].[Mesto] ([MestoId]),
	CONSTRAINT [FK_ZapisnikLicitacije_Nadmetanje] FOREIGN KEY ([OglasId],[Rb]) REFERENCES [dbo].[Nadmetanje] ([OglasId],[Rb])
)

CREATE TABLE [dbo].[StavkaZapisnikaLicitacije](
	[ZapisnikLicitacijeId]	INT			NOT NULL,
	[Rb]					INT			NOT NULL,
	[Pobednik]				BIT			NULL,
	[LicitiranaCena]		DECIMAL(12,2)	NULL,
	[BrLicitatorskeKartce]	INT			NULL,
	[Prisutan]				BIT			NULL,
	[PrigovorPrimedba]		NVARCHAR(MAX)	NULL,
	[Odluka]				NVARCHAR(MAX)	NULL,
	[PrijavaId]				INT			NOT NULL,
	CONSTRAINT [PK_StavkaZapisnikaLicitacije] PRIMARY KEY CLUSTERED ([ZapisnikLicitacijeId],[Rb]),
	CONSTRAINT [FK_StavkaZpisnikaLicitacije_ZapisnikLicitacije] FOREIGN KEY ([ZapisnikLicitacijeId]) REFERENCES [dbo].[ZapisnikLicitacije] ([ZapisnikLicitacijeId])
	ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT [FK_StavkaZapisnikaLiciacije_Prijava] FOREIGN KEY ([PrijavaId]) REFERENCES [dbo].[Prijava] ([PrijavaId])
)