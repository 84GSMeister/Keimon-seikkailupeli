# Keimon Seikkailupeli

## Uusin versio (28.6.2023): 0.8

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
