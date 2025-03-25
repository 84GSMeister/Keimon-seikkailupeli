# Keimon Seikkailupeli

## Uusin versio (25.3.2025): 1.0.1 Alfa

### Versio 1.0.1 Alfa

 - Lisätty pelaajan vinokävelyanimaatiot
 - Vinosuuntaan käveleminen on nyt hitaampaa (nopeus/√2)
 - Lisätty popup-ruutu ilmaisemaan suoritettua tavoitetta
 - Lisätty kuvaavammat vinkkitekstit tavoitteen puuttumiselle kun yrittää kulkea oviruudusta
 - Korjattu tekstin rivinvaihto-ongelmat valikkoruuduissa
 - Korjattu 3D-elementtien toimivuus vanhoilla Shader-versioilla
 - Korjattu vihollisten reitinhaun aiheuttama lagi temppelihuoneessa
 - Poistettu hyödyttömiä näppäinvinkkitekstejä (mm. portti). Pelaaja pystyy edelleen katsomaan asioita C-näppäimellä, vaikka vinkkitekstiä ei tulekaan.
 - Lisätty Keimo3D-simulaattori minipeliksi Keimo-baariin
 - Kaatuminen muistivuotojen johdosta pääosin korjattu
 - Pelattavuus vanhalla pelimoottorilla korjattu.
    - Vanha pelimoottori tullaan poistamaan versiossa 1.1
	- Tarkoituksena on säilyttää versio 1.0.x pelattavana myös tietokoneilla, joiden näytönohjain ei tue OpenGL 3.0:a.
	- Puuttuvia graafisia ominaisuuksia ei tulla toteuttamaan vanhassa pelimoottorissa.

### Versio 1.0 Alfa

 - Uusi grafiikkamoottori (OpenGL- ja Shader-pohjainen). Ominaisuuksia mm:
	- Huomattavasti parempi suorituskyky ja rajaton huoneen koko (muistin määrän puitteissa)
	- Kokonäyttö (F11) ja ikkunan koon vapaa muuttaminen
	- Kentän skaalaus, zoomaus, kääntö
	- 3D-objektit
	- liikkuvat ja pyörivät objektit
	- Elementtien päällekkäisyys (mm. kenttä ja HUD)
	- Häivytysefekti (huonetta vaihtaessa)
	- Tummennusefekti (Velhometsä) sekä muut väriefektit
	- Todella ruma tekstin renderöinti (toistaiseksi, tullaan varmasti parantamaan myöhemmin)
	Vanhaa pelimoottoria pystyy vielä käyttämään toistaiseksi ongelmatilanteissa.
 - Uusi alue: Keimo-baari
 - Uusi alue: Yo-kylä Itä (Asuintalot rakennettu uudelleen paremmassa mittakaavassa)
 - Uusi alue: Temppeli
 - Uudet musat: koti, kauppa, metsä, baari ja temppeli
 - Uusia tavoitteita: SPOILER! Pelaajan täytyy nuotion sytyttämisen jälkeen etsiä Keimo-baari, jutella Pasille, etsiä Velhometsä ja päihittää pomo.
 - Kaljasta saa lyhytaikaisen kuolemattomuuden (hyödyllinen varsinkin pomotaistelussa)
 - Lisätty vinkkitekstit näppäimille, kun pelaaja on esineen tai kiintopisteen kohdalla
 - Lisätty katsottava kartta-esine
 - Asetus- ja kehittäjävalikko uudistettu
 - Collision-tarkistus muutettu niin, että se tarkistaa pelaajan ja laatan hitboxin (neliö) leikkauksen, eikä vain pelaajan sijaintia (pelaaja ei enää voi kävellä puoliksi laatan sisään).
 - Editoriin lisätty zoomaus
 - Editorissa voi päättää pelaajan aloitushuoneen ja -sijainnin (tallentuu kst-tiedostoon)
 - Korjattu editorin ongelma luodessa tasoja tyhjän huonekartan päälle
 - Dialogit tallentuvat nyt kst-tiedostoon. Muokkaukset dialogeihin voi tallentaa.
 - Lisätty kilinä-ääni poimiessa kaljaa ostoskoriin
 - Lisää tölkkiääniä
 
### Versio 0.9.2

 - Lisätty Pomohuone Velhometsään
 - Hyökkäysmekaniikat uudistettu: Nyt pelaaja voi lyödä ilmaa aseella, ja jos lyönnin aikana osuu viholliseen, vihollinen ottaa vahinkoa
 - Editorissa voi lisätä entityjä (NPC:t ja muut entityt nyt samassa valikossa). Valikkoon lisätty myös kuvakkeet.
 - Entityt voivat olla kaiken kokoisia. Lisätty iso laatikko.
 - Ämpärijonosta voi poistua (Korjattu dialoginvalinnan sivuvaikutukset ämpärijonoruudussa)
 - Lisätty ääniefektit: kerääminen, pudottaminen, hyökkäys, esineen käyttäminen, jallupullo
 - Lisätty ääniefektit alkuvalikkoon ja tarinaruutuun
 - Lisätty äänitesti-ikkuna
 - Musat vaihtuvat huonekohtaisesti (vanha musavalikko siirretty äänitestin taakse (Peli -> Asetukset -> Avaa äänitesti))
 - Korjattu tappolaskuri vastaamaan kukistettuja vihollisia lyöntien määrän sijaan
 - Valintaruutujen (dialogivalinta, ämpärijono) GUI hieman uudistettu
 - Korjattu virheelliset tilet ja objektit kotoa
 - Lisää tölkkiääniä

### Versio 0.9.1: Keimon nimipäiväpäivitys

 - Lisätty Enityt: Työnnettävät laatikot
 - NPC:t, ammukset ym. kuuluu nyt entityjen alaluokkiin
 - Entityjä voi spawnata F6-näppäimellä. TODO: Entityt editoriin
 - Lisätty Tavoite-editori
 - Placeholder cutscene -teksti korvattu Keimon Seikkailupelin logolla
 - Korjattu tarinadialogin puuttuminen kotiin saapuessa
 - Korjattu näkymän hyppääminen huoneen yläreunaan latauksen aikana
 - Poistettu editorin karttaominaisuus (jäi turhaksi huonekokopäivityksen myötä ja oli muutenkin hyvin rikkinäinen)
 - Kokeellinen: Lisätty Keimo3D Demo

### Versio 0.9

 - Mukautettava huoneen koko: Automaattinen scrollaus ja yli 10-kokoiset huoneet
 - Tasot suunniteltu uudelleen hyödyntäen em. ominaisuutta: Jokainen entinen alue on nyt 1 iso huone
 - Automaattisesti generoidut minimapit
 - Kerättävät esineet: Kolikko
 - Uusi esine: Jallupullo
 - Uusi esine: Puiston penkki - Toimii kuten sänky, mutta nukkumisesta humalassa menettää enemmän HP:ta
 - Custom-fontti
 - Asetusvalikkoa voi selata pelin sisällä (Pause -> Asetukset)
 - Visuaalisilla objekteilla (a.k.a. Koriste-esineillä) voi olla katsomisdialogi (kuten kenttä-NPC:illä). Dialogeja voi muokata dialogieditorissa.
 - Dialogieditorissa voi nyt myös luoda ja muokata valintoja
 - Skaalaus koko näytölle toteutettu uudelleen ja useimmat graafiset ongelmat korjattu
 - Ääniefekteillä voi olla suunta ja voimakkuus riippuen äänenlähteen sijainnista pelaajan / ruudun keskipisteen suhteen
 - Editorissa voi päättää huoneen koon luontivaiheessa
 - Editoria voi scrollata hiiren oikealla
 - Editorissa objektien kohdalla näkyy niiden kuvake
 - Editori skaalautuu paremmin ikkunan koon muutokselle
 - Mediantoisto-ominaisuuksia jälleen parannettu: Eri mediatoistin eri äänitiedostoille: wave-toisimessa on tarkka loop-kohta, mutta mp3- ja ogg-toistimessa voi säätää volyymia tarkemmin.
 - Osa Pelisäikeen ja Grafiikkasäikeen koodista yrittää viimein toimia edes jollain tasolla säieturvallisesti
 - Uusi Debug Info -ikkuna
 - Lisää tölkkiääniä

### Versio 0.8.6

 - Dialogieditori
 - Kenttä-NPC:ille voi määrittää Custom-dialogin
 - Editorissa voi nyt tallentaa yksittäisiä huoneita .ksh-tiedostoon. Huoneen voi tuoda minkä tahansa olemassaolevan huoneen päälle. ksh-tiedoston formaatti on sama kuin kst-tiedoston yksittäisessä huoneessa, mutta id jätetään huomioimatta.
 - Korjattu kauppaoven aiheuttama kaatuminen (jos kaupasta poistuu tyhjän inventoryn kanssa)
 - Korjattu editorin virhe NPC:n poistamisessa

### Versio 0.8.5

 - Asevihut
 - Pomo
 - Vihollisilla on elämäpisteet ja aseilla vahinkopisteet (ei enää niin, että ase tappaa vihollisen ensimmäisellä lyönnillä)
 - NPC:iden tiedot-kohta editorissa näyttää enemmän tietoja (kuten hp, vahinko ja tehoavat aseet)
 - Viholliset osaa hakea reitin pelaajaa kohti, jos vihollisen liiketavaksi on asetettu "seuraa reittiä"
 - Muita uusia liiketapoja viholliselle: ympyräliike ja boss-liike
 - Portit, napit ja vihollistyyppikohtaiset painelaatat
 - Jälleen uusia Udo haukkuu -remixejä
 - Latausikkuna

### Versio 0.8.4

 - Tarinaedinaeditori: Editorissa voi itse luoda omia tarinapätkiä, määritellä niille sivumäärän, ja asettaa jokaiselle sivulle kuvan ja tekstin. Tarinapätkät tallentuvat kst-tiedostoon huoneiden jälkeen.
 - Editorin karttatoimintoa parannettu: nyt sitä voi vetää hiirellä ja zoomata
 - Lisätty Puuovi (kuin tavallinen oviruutu, mutta pitää äänen ja voi vaihtaa kuvaketta avatessa)

### Versio 0.8.3: Keimon nimipäiväpäivitys

 - Minimap (ei toimi vielä kaikkialla, mm. sisätiloissa)
 - Uusia Udo haukkuu -remixejä
 - Huoneen metatietojen muokkausta editorissa tehty selkeämmäksi
 - Velhometsää muutettu hieman sokkelomaisemmaksi

### Versio 0.8.2

 - Goblin, Jumal Yoda ja Jumal Velho
 - Velhometsän ensimmäiset ruudut
 - Goblin "paljastuu" Jumal Yodaksi kun juttelee ensimmäisen kerran Jumal Velholle
 - Editoriin lisätty ominaisuus, jolla voi tarkastella huoneiden maastoa jatkuvana karttana (toimii vain reunawarppien kautta huoneille, jotka ovat kiinni huoneessa 0)
 - Peli ei vaadi enää Javan asennusta

### Versio 0.8.1

 - GUI skaalautuu kokonäytölle (erittäin buginen vielä)
 - Vuoropuhedialogissa voi tulla valintoja
 - Sillalta voi hypätä
 - Musiikin voimakkuus muuttuu "lennosta" säädettäessä
 - Ajastin pysähtyy pausettaessa
 - Pullonpalautusautomaatissa voi tulla useita erilaisia virheitä
 - Editorissa maaston kuvat kääntyvät ja peilautuvat oikein myös muilla välilehdillä (Kuvakkeilla on nyt mahdollista olla läpinäkyvyys, rotaatio ja skaalaus samanaikaisesti)
 - Korjattu editorin bugi, jossa kauppahyllyjen käännetty kuvake ei päivity oikein sisältömuutoksen jälkeen
 - Poistettu maastogeneraattori

### Versio 0.8

 - Uusi alue: Kauppa
 - Kaupasta voi ostaa sekä yrittää varastaa tavaroita (juoksukalja)
 - Uusi NPC: Vartija - Käy kimppuun jos yrität varastaa tavaroita kaupasta
 - Tilet, joita voi kävellä vain yhteen suuntaan. Suuntaa voi muuttaa editorissa. Jos tiedostonimi päättyy "_y", tile muuttuu yksisuuntaiseksi
 - Uusi esine: Pontikka-ainekset (voi ostaa kaupasta)
 - Kiintopiste-objekteja voi kääntää editorissa
 - Känniefekti vaikuttaa myös pelaajan liikkumiseen
 - Musiikin ja ääniefektien voimakkuutta voi säätää
 - Sivupaneelin sisältöä GUI:ssa voi muuttaa alueen mukaan, esim. jos alue on "Kauppa", kontrolli-infopanelin tilalle vaihtuu ostoskori.
 - Viholliset liikkuvat joka tickillä (ei enää joka toisella)
 - Vihollisten nopeuksia (pikseliä/tick) muutettu: pikkuvihu=3, pahavihu=4, vartija=9 (pelaaja=8)
 - Pelaajan nopeus muuttuu ostoskorin sisällön mukaan. Jokainen ostos pienentää nopeutta yhdellä. Vakionopeus=8, täyden ostoskorin kanssa nopeus =2
 - Mediantoisto-ominaisuuksia laajennettu: nyt myös mm. mp4-videot ja wav-äänet on tuettu, musiikki voi loopata muualta kuin alusta ym.
 - Suorituskykyasetus framerate- ja tickrate-tarkkuudelle. Nopea asetus huonontaa tarkkuutta, mutta pienentää CPU-käyttöä.

### Versio 0.7.3

 - Känniefekti
 - Kännin voi nukkua pois (krapula vähentää elämäpisteitä). Nukkuminen nollaa myös lihavuuden.
 - Monivaiheiset dialogit (kauppa)
 - Lähikuvat kenttäkohteille dialogipaneelissa (Juhani, Kauppias)
 - Ämpäreitä voi jonottaa
 - Pullonpalautusautomaatti, joka jää jumiin 10% todennäköisyydellä
 - Liikkumisnäppäimen pohjassa pitäminen toimii nyt myös oviruutujen kohdalla
 - Musiikkia voi vaihtaa ilman uudelleenkäynnistystä
 - Häviöruudussa odotusaika ennen kuin vaihtoehdot tulevat saataville (estää häviöruudun animaation kelaamisen vahingossa)
 - Pelaajan suunta tarkistetaan liikesuunnan eikä näppäinpainallusten perusteella -> pelaaja ei voi enää moonwalkata :(
 - Uusi kuvake hiilelle

### Versio 0.7.2

 - Uudet häviöanimaatiot pahavihulle
 - Uudelleenkäynnistäminen ei enää avaa uutta ikkunaa
 - Asetusvalikkoa parannettu: rajat numeerisille arvoille
 - Collision-tarkistus tapahtuu joka tickillä. Tick-nopeutta voi säätää.
 - Säielogiikkaa parannettu -> pienempi CPU-käyttö
 - Korjattu editorin bugi, jossa reunawarpit eivät toimi huoneille, joiden ID on yli huonelistan koon (ts. tyhjiä indeksejä välissä).

### Versio 0.7.1

 - Keimon kämppä, paljon uusia tilejä
 - Pahavihulle uusi sprite
 - Vihollisten kuvakkeet peilautuvat automaattisesti liikesuunnan mukaan
 - Dialogiteksti kun käyttää esineitä (pl. olut), dialogitekstin voi kelata
 - Korjattu jumiin jäävät näpppäimet tarinaruudussa
 - Huoneen latausnopeutta optimoitu
 - Editorissa maasto näkyy taustalla myös objekti/npc -muokkaustilassa + editorin ulkoasua hieman parannettu ja visuaalisia bugeja korjattu
 - Editorissa voi kääntää ja peilata myös koriste-esineitä
 - Korjattu editorin bugi, jossa objektien peilaus ei lataudu + bugi, jossa objekteja ei voi kopioida tyhjien ruutujen päälle

### Versio 0.7

 - Uusi GUI
 - Paljon uusia tilejä asuinalueen seudulle
 - Dialogipaneelit esineitä katsoessa ja NPC:ille jutellessa
 - Tarinadialogi, kun saapuu uuteen huoneeseen
 - Koriste-esineet, joiden kuvan voi valita. Koriste-esineiden tarkoitus on sopia sulavasti maasto-tilejen päälle ja ne voivat estää kävelyn.
 - Editorissa voi perua viimeisimmän muutoksen (CTRL + Z), kopioida esineen (CTRL + hiiren vasen) sekä kääntää ja peilata maasto-tilejen kuvan

### Versio 0.6.5

 - Vihollisille voi asettaa eri liikemalleja, mm. Seuraa pelaajaa (nykyisellään ei osaa vielä väistää esteitä)
 - Pahavihu korjattu editorissa
 - Editorissa voi asettaa reunawarpin huoneisiin (Reunawarp ei ole objekti vaan koko huoneen reuna warppaa asetettuun huoneeseen)
 - Editorin navigointia tehty helpommaksi: Jokaisessa suunnassa nuolet, joita klikkaamalla pääsee reunawarpin suuntaiseen huoneeseen. Myös uusien huoneiden luonti tehty selkeämmäksi.

### Versio 0.6.4

 - Uudet häviöanimaatiot (kuolemat viholliselle, ylensyönti ja Juhani)
 - Pelikentän koko reuna voi warpata huoneeseen (warppi tallentuu .kst-tiedostoon, mutta editorista puuttuu vielä toiminto, jolla sen voisi asettaa)
 - Pelikenttää (default.kst) jatkettu uusilla maisemilla (suunnitelma)
 - Rainbow lager -tölkit, joita keräämällä voi ansaita rahaa -> rahan voi antaa Juhanille

### Versio 0.6.3

 - Animoitu pelaajan liikkumiskuvake
 - Tarinan alkua hieman uusittu
 - Asetettu näkyvät nimet ja katsomistekstit setelille ja huumeelle
 - Korjattu pelin kaatuminen asetusten vaihtamisen jälkeen sekä huone-editorin bugi "Älä näytä uudelleen" painamisen jälkeen
 - Korjattu bugi, jossa huone-editori ei tallenna arkun sisältöä
 - Korjattu bugi alkuvalikosta, jossa visuaalinen osoitin jää väärään kohtaan asetusten hyväksymisen jälkeen

### Versio 0.6.2

 - Uudet animoidut kuvakkeet pelaajalle ja joillekin objekteille
 - Eri loppuruudut erilaisille kuolemille
 - Juhanin lyöminen pesäpallomailalla johtaa välittömään kuolemaan
 - Kommentointia hieman parannettu (vielä erittäin puutteellinen)

### Versio 0.6.1

 - Loppuruudut
 - Arkuista voi saada esineitä (editorissa voi laittaa arkkuun esineen)
 - Editorissa voi tallentaa taustan
 - Pelaajan kädessä olevan esineen kuvake näkyy pelaajan kohdalla
 - Kilpi korjattu
 - Pelikentän GUI skaalautuu keskelle kokoruudulla

### Versio 0.6

 - Uudet tilet
 - Animoitu pelaajan kuvake
 - Pelaaja liikkuu lineaarisesti (ei enää ruutujen mukaan)
 - Viholliset liikkuvat lineaarisesti
 - Collision-tarkistus uudistettu: perustuu neliöiden leikkauksiin
 - Editorissa välilehti NPC:ille + editorin objektien muokkausominaisuuksia hieman laajennettu
 - Pari uutta ääniefektiä

### Versio 0.5.4

 - Juhani, huume ja kahen kybän seteli
 - pesäpallomaila
 - vihuja voi lyödä pesäpallomailalla
 - editori vaihdettu tallentamaan tiedostoja UTF-8-formaatissa. vanhat .kst-tiedostot eivät välttämättä enää lataudu.
 
### Versio 0.5.3

 - editoriin lisätty maaston muokkaus ja kuvien haku kansiosta
 
### Versio 0.5.2

 - editorin bugeja korjattu
 - editori tallentaa nyt myös ominaisuudet objekteille

### Versio 0.5.1

 - koodia hieman refaktoroitu ja buildi korjattu

### Versio 0.5

 - huone-editori
 - guita hieman muutettu skaalautuvammaksi pienille näytöille

### Versio 0.4.2

  - uusi alkuvalikko
  - ensimmäiset kenttäruudut suunnitelman perusteella
  - teemat (puisto)
  - tarinaa muokattu
  
 ### Versio 0.4.1
 
  - kenttäkohde- ja maastogeneraattori (perustuu kuvatiedoston väriarvoihin)

### Versio 0.4

  - alkuvalikko
  - liikkuvat viholliset
  - maastotyypit
  - pelin voi pausettaa

### Versio 0.3.2:
  - fps-ongelmaa parannettu jälleen hieman
  - määritetty sijainti objekteille

### Versio 0.3.1:
  - huoneita voi luoda ja niihin uusiin huoneisiin voi warpata valikosta
  - taustat
  - objektien reunat saa pois näkyvistä/näkyviin
  - FPS-ongelma osittain korjattu
  
### Versio 0.3:
  - useita huoneita
  - huonetta voi vaihtaa oviruutujen avulla

### Versio 0.2.2
  - lisätty asetusvalikko ja musiikit
  
### Versio 0.2.1
  - uudet kuvakkeet

### Versio 0.2
  - lisätty graafinen käyttöliittymä
  - pelaajaa voi ohjata näppäimillä
