# Vaatimusmäärittely


## Sovelluksen käyttäjät

Sovelluksen avulla käyttäjä pystyy pitämään kirjaa eri kursseilla (tai muunlaisissa projekteissa) käyttämästään ajasta ja tallentamaan tuntikirjanpito markdown-tiedostoina

## Käyttäjät

Sovelluksessa on vain yksi käyttäjärooli (*käyttäjä*)

## Käyttöliittymäluonnos

Ohjelman pääikkunassa on taulukkoesitys tuntikirjanpidosta sekä mahdollisuudet suodattaa kirjauksia projektien mukaisesti.

Tehtävän kirjaamiselle ja aikaseurannalle on oma näkymänsä

Apudialogeja tarvittaneen ainakin uuden projektin lisäämiselle.


## Perusversion tarjoama toiminnallisuus

- Kun ohjelma käynnistyy, luetaan kaikkien projektien työaikakirjanpidot ja niistä muodostetaan käyttäjälle esitettävä yhteenveto
- Käyttäjä voi tarkastella näkymää ajan mukaan tai tarkastella yksittäisen projektin kirjauksia 
- Käyttäjä voi lisätä projekteja (uusi työajanseuranta taikka olemassa olevan md-tiedoston tuominen) 
- Tuntikirjanpidon voi tallentaa
  - markdown-tiedostoksi
  - SQLite-tietokantaan 

- Käyttäjä tekee kirjauksen
  - syöttämällä päivämäärän, tunnit ja tehtävän tai
  - käyttämällä ajanottopainikkeita tai
  - jatkamalla (muokkaamalla) aiempaa tehtävää

- Käyttäjä voi korjata aiempia kirjauksia


## Jatkokehitysideoita

- vaihtoehtoisia tallennusbackendeja (csv, excel, Google Calendar API)
- erilaiset ajankäytön raportit
- mahdollisuus viedä projekti tallennusjärjestelmästä toiseen (esim. tuottaa tietokantaan tallennetusta projektista markdown-tiedosto)
- integroitu git-asiakas, joka yhdistää committoimisen ja tehtävän kirjaamisen :smiley:
