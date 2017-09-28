## Projekt u sklopu diplomskog rada na temu "Uzorci dizajna za podatkovni sloj"
Ovaj repozitorij sadrži aplikaciju koja je rađena u sklopu diplomskog rada na temu "Uzorci dizajna za podatkovni sloj." Radi se minimalističkoj aplikaciji za upravljanje ljudskim resursima koja pokazuje implementaciju odabranih uzoraka dizajna opisani u diplomskog radu, a koji su preuzeti iz knjige Martina Fowlera "Patterns of Enterprise Application Architecture."

### Korišteni uzorci dizajna
<dl>
<dt><em>Data Mapper</em></dt>
<dd>Predstavlja sloj objekata koji vrše preslikavanje (eng. <em>mapping</em>) podataka između podatkovnih objekata i podataka u bazi podataka.</dd>
<dt><em>Identity Map</em></dt>
<dd>Sprečava višestruko učitavanje istog podatkovnog objekta u više objekata tako da se prilikom prvog njegovog učitavanja objekt sprema u mapu. Tada, svaki put kada se želi opet učitati taj podatak, učitava se iz mape.</dd>
<dt><em>Lazy Load</em></dt>
<dd>Radi se o objektu koji nema u sebu inicijalizirane sve podatke, ali zna kako doći do njih. Odnosno, odgađa dohvaćanje pojedinih (ili svih) podataka sve dok nisu potrebni.</dd>
<dt><em>Embedded Value</em></dt>
<dd>Jedan objekt iz podatkovnog objekta rastavlja se u više polja u relacijskoj tablici. Ovaj uzorak dizajna se koristi uglavnom u slučajevima kada se radi o malom objektu (npr. objekt koji predstavlja plaću radnika), ali nema smisla kreirati zasebnu tablicu u bazi podataka.</dd>
<dt><em>Value Object</em></dt>
<dd>Omogućuje da se identitet objekta ne temelji na referenci, već na temelju vrijednosti.</dd>
</dl>
