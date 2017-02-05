# DE-IK PTI-MSc - Fejlett adatbázis technológiák - 2016

## Fejlesztők
* Czuczor Gergő
* Kiss Sándor Ádám
* Salagvárdi Tibor

### Windows-ra vonatkozó telepítési és használati útmutató

#### BaseX adatbázisszerver telepítése

* Töltsük le a BaseX 8.6 adatbázisszerver telepítőjét: http://files.basex.org/releases/8.6/BaseX86.exe
* Telepítstük fel a letöltött adatbázisszervert.
* Indítsuk el a BaseX szervert: C:\Program Files (x86)\BaseX\bin\basexserver.bat
* Indítsuk el a BaseX klienst: C:\Program Files (x86)\BaseX\bin\basexclient.bat
* Jelentkezzünk be "admin/admin" felhasználónév/jelszó párossal.
* Másoljuk át a universe\src\main\resources\fejlett_adatbazis_hazi.xml állományt a D:\ meghajtó gyökerébe.
* A BaseX kliensen belül hozzuk létre az adatbázis sémát az alábbi parancs kiadásával: ```CREATE DB universe D:\fejlett_adatbazis_hazi.xml```
* Zárjuk be a BaseX klienst.

#### Az alkalmazás lefordítása

Windows parancssorban navigáljunk el a projekt főkönyvtárába, majd adjuk ki egymás után az alábbi parancsokat:
* ```cd universe-model```
* ```mvn clean install```
* ```cd ..\universe```
* ```mvn clean install```

#### Az alkalmazás elindítása

Windows parancssorban navigáljunk el a projekt főkönyvtárába, majd adjuk ki egymás után az alábbi parancsokat:
* ```cd universe```
* ```mvn clean package exec:java```

# Linkek:

http://docs.basex.org/wiki/XQuery_3.0

http://docs.basex.org/wiki/Validation_Module#XML_Schema_Validation

