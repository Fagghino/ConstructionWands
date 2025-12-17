# 🏗️ ConstructionWands

[![Version](https://img.shields.io/badge/version-1.7.0-blue.svg)]()
[![Minecraft](https://img.shields.io/badge/minecraft-1.20+-green.svg)](https://www.minecraft.net/)
[![License](https://img.shields.io/badge/license-MIT-yellow.svg)](LICENSE)

**ConstructionWands** è un plugin Spigot/Paper per bacchette di costruzione personalizzate con supporto completo per SuperiorSkyblock2. Permette di creare bacchette per piazzare blocchi in griglie di dimensioni configurabili con controllo granulare delle protezioni.

## 📋 Descrizione

ConstructionWands è un plugin Minecraft che permette di creare bacchette personalizzate per piazzare blocchi in griglia. Supporta configurazione avanzata per range, altezza, cooldown, e integrazione completa con SuperiorSkyblock2 per la gestione delle protezioni delle isole.

## ✨ Caratteristiche Principali

### 🪄 Bacchette Personalizzabili
- **Range configurabile**: Piazza blocchi in griglie da 1x1 fino a NxN
- **Altezza (length)**: Controlla lo spessore della griglia piazzata
- **Cooldown**: Previeni lo spam con delay personalizzabili per ogni bacchetta
- **Usi limitati o infiniti**: Configura bacchette consumabili o permanenti
- **Custom model data**: Supporto per texture pack personalizzati
- **Non stackabili**: Ogni bacchetta è unica grazie a UUID univoci
- **Left-click action configurabile**: Scegli la funzione del click sinistro (NONE/UNDO/MODE)

### 🎯 Modalità di Piazzamento
- **AUTO** (default): Direzione basata sulla faccia del blocco cliccato
- **VERTICALE**: Piazzamento sempre verticale (asse Y), indipendentemente dalla faccia cliccata
- **ORIZZONTALE**: Piazzamento sempre orizzontale (piano XZ), indipendentemente dalla faccia cliccata
- **Cambio modalità**: Con click sinistro se `left-click-action: MODE` è configurato
- **Persistenza**: Modalità salvata per ogni player e per ogni bacchetta individualmente

### 🔄 Sorgente Blocchi
- **Offhand**: Prende blocchi dalla mano secondaria (comportamento classico)
- **Inventory**: Prende blocchi dall'inventario dello stesso tipo del blocco cliccato

### 🔒 Protezioni e Integrazioni
- **World Border**: Rispetta sempre i limiti del mondo, anche con bypass admin
- **SuperiorSkyblock2**: Integrazione completa con controllo permessi
  - Proprietari e membri possono costruire sulle proprie isole
  - Supporto coop players
  - Permessi personalizzati tramite `/is permission`
  - Bypass admin con `/is admin bypass` per gli staffer
  - **Conteggio blocchi isola**: I blocchi piazzati vengono correttamente tracciati per il calcolo del livello isola
- **Altri plugin di protezione**: Compatibile con WorldGuard, GriefPrevention, ecc. tramite BlockPlaceEvent

### ⚙️ Comandi
- `/wand <nome_bacchetta>` - Ottieni una bacchetta per te stesso
- `/wand <nome_bacchetta> <nome_player>` - Dai una bacchetta a un giocatore (anche da console)
- `/wand <nome_bacchetta> <nome_player> <quantità>` - Dai multiple bacchette

### 🔑 Permessi
- `constructionwands.give` - Permesso per eseguire il comando /wand (default: op)
- `constructionwands.use` - Permesso per usare le bacchette (default: true)

## 🚀 Installazione

1. Scarica l'ultima versione del plugin
2. Copia il file `.jar` nella cartella `plugins` del server
3. (Opzionale) Installa SuperiorSkyblock2 per il supporto isole
4. Avvia/riavvia il server
5. Modifica `config.yml` e `wands.yml` per personalizzare le bacchette

## 📝 Configurazione

Il plugin utilizza due file di configurazione separati:
- **`config.yml`** - Configurazioni generali (blocchi protetti, messaggi, timeout undo)
- **`wands.yml`** - Definizioni di tutte le bacchette disponibili

### Esempio Bacchetta (wands.yml)

```yaml
wands:
  iron_wand:
    name: "&fBacchetta di Ferro"
    model-data: 1002
    lore:
      - "&7Range: 3x3x1"
      - "&7Click destro per piazzare"
      - "&7Click sinistro per cambiare modalità"
      - "&eUSI: {uses}"
    range: 3           # Larghezza/profondità della griglia (1 = 1x1, 3 = 3x3, 5 = 5x5, ecc.)
    length: 1          # Altezza della griglia (numero di layer)
    delay: 500         # Cooldown in millisecondi (0 = nessun delay, 1000 = 1 secondo)
    source: offhand    # Da dove prendere i blocchi: "offhand" o "inventory"
    type: IRON_INGOT   # Tipo di item della bacchetta
    uses: 100          # Usi disponibili (-1 per infinito)
    infinite: false    # true = usi infiniti
    left-click-action: MODE  # Funzione click sinistro: NONE, UNDO, o MODE
```

### Parametri Bacchetta

- **name**: Nome visualizzato (supporta codici colore con `&`)
- **model-data**: ID per custom model data (texture pack)
- **lore**: Descrizione della bacchetta (lista di stringhe)
  - Usa `{uses}` per mostrare gli usi rimanenti
- **range**: Dimensione della griglia (es. 3 = griglia 3x3)
- **length**: Altezza/spessore (es. 2 = due layer di blocchi)
- **delay**: Cooldown in millisecondi per prevenire spam
- **source**: 
  - `offhand` - Usa blocchi dalla mano secondaria
  - `inventory` - Usa blocchi dall'inventario dello stesso tipo del blocco cliccato
- **type**: Tipo di item Minecraft (BLAZE_ROD, STICK, ecc.)
- **uses**: Numero di utilizzi (-1 per infinito)
- **infinite**: true/false per usi infiniti
- **left-click-action**: Funzione del click sinistro (default: NONE)
  - `NONE` - Click sinistro non fa nulla
  - `UNDO` - Annulla l'ultimo piazzamento (se blocchi non modificati)
  - `MODE` - Cambia modalità di piazzamento (AUTO → VERTICALE → ORIZZONTALE)

### Messaggi Personalizzabili

```yaml
messages:
  no-permission: "&cNon hai il permesso per usare questo comando!"
  wand-given: "&aHai ricevuto una {wand}!"
  wand-not-found: "&cBacchetta non trovata!"
  no-blocks: "&cNon hai abbastanza blocchi nella mano secondaria!"
  no-blocks-inventory: "&cNon hai abbastanza blocchi nell'inventario!"
  no-island-permission: "&cNon hai il permesso per costruire su questa isola!"
  cooldown: "&cDevi aspettare prima di usare nuovamente la bacchetta!"
  uses-depleted: "&cLa bacchetta ha esaurito gli usi!"
  blocked-block: "&cNon puoi usare la bacchetta su questo tipo di blocco!"
  mode-auto: "&aModalità di piazzamento: &eAUTO"
  mode-vertical: "&aModalità di piazzamento: &eVERTICALE"
  mode-horizontal: "&aModalità di piazzamento: &eORIZZONTALE"
```

### 🚫 Blocchi Protetti

Puoi configurare una lista di blocchi sui quali le bacchette **non possono funzionare**, impedendo il piazzamento anche se il giocatore ha tutti i permessi necessari.

```yaml
blocked-blocks:
  - SPAWNER
  - BEDROCK
  - BARRIER
  - COMMAND_BLOCK
  - CHAIN_COMMAND_BLOCK
  - REPEATING_COMMAND_BLOCK
  - STRUCTURE_BLOCK
  - JIGSAW
  - END_PORTAL_FRAME
```

**Caratteristiche:**
- ❌ Blocca l'uso della bacchetta quando si clicca su un blocco nella lista
- ✅ Validazione automatica: ignora i blocchi non validi nel config
- 📝 Log avvisi in console per blocchi non riconosciuti
- 💬 Messaggio personalizzabile: `blocked-block`
- 🔒 Protezione aggiuntiva per blocchi critici del server

**Esempio d'uso:**
Se configuri `SPAWNER` nella lista e clicchi con la bacchetta su uno spawner, riceverai il messaggio configurato in `blocked-block` e l'azione verrà annullata.

## 🔧 Requisiti

- **Server**: Spigot/Paper 1.20.1+
- **Java**: 17+
- **Dipendenze opzionali**:
  - SuperiorSkyblock2 (per protezione isole)

## 📦 Dipendenze Maven

```xml
<dependencies>
    <dependency>
        <groupId>org.spigotmc</groupId>
        <artifactId>spigot-api</artifactId>
        <version>1.20.1-R0.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>com.bgsoftware</groupId>
        <artifactId>SuperiorSkyblockAPI</artifactId>
        <version>2023.3</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## 🤝 Supporto SuperiorSkyblock2

Il plugin si integra automaticamente con SuperiorSkyblock2 se presente sul server.

### Chi può usare le bacchette sulle isole:
✅ Proprietario dell'isola  
✅ Membri del team  
✅ Coop players (con `/is coop <nome>`)  
✅ Giocatori con permessi personalizzati (con `/is permission <nome>` con privilegio BLOCK_PLACE)  
✅ Staffer con bypass admin attivo (`/is admin bypass`)  
❌ Visitatori senza permessi  

### Messaggi di Errore
- Se provi a usare la bacchetta su un'isola dove non hai permessi, riceverai il messaggio configurato in `no-island-permission`

## 📝 Changelog

### **Versione 1.7.0** (Corrente) 🔄
- ✨ **MAJOR:** Sistema di modalità di piazzamento configurabili
- ✨ **MAJOR:** BlockPlaceEvent per compatibilità completa con SuperiorSkyblock2
- ✨ **Nuovo:** Opzione `left-click-action` configurabile (NONE/UNDO/MODE)
- ✨ **Nuovo:** Tre modalità di piazzamento: AUTO, VERTICALE, ORIZZONTALE
- ✨ **Nuovo:** Cambio modalità con click sinistro quando `left-click-action: MODE`
- ✨ **Nuovo:** Persistenza modalità per ogni player e bacchetta individualmente
- ✨ **Nuovo:** Messaggi configurabili per cambio modalità (mode-auto, mode-vertical, mode-horizontal)
- 🔧 **Migliorato:** Blocchi piazzati triggerano BlockPlaceEvent per SS2 e altri plugin
- 🔧 **Migliorato:** SuperiorSkyblock2 ora conta correttamente i blocchi per il livello isola
- 🔧 **Ottimizzato:** Sistema left-click unificato con switch case
- 🔧 **Ottimizzato:** Calcolo direzione piazzamento basato su modalità selezionata
- 📚 **Documentazione:** Nuova sezione "Modalità di Piazzamento" nel README
- 📚 **Documentazione:** Aggiornati esempi wands.yml con commenti dettagliati
- 📚 **Documentazione:** Spiegazione completa left-click-action e placement modes
- 🎨 **Pulizia:** Rimosso parametro `enable-undo` (sostituito da left-click-action)
- 🐛 **Risolto:** Blocchi piazzati con wand non conteggiati nel livello isola SS2
- 🐛 **Risolto:** Eventi BlockPlaceEvent non triggerati correttamente

### **Versione 1.6.0** 📂
- ✨ **MAJOR:** Sistema di configurazione file separati
- ✨ **Nuovo:** File `wands.yml` dedicato per le definizioni bacchette
- ✨ **Nuovo:** Caricamento automatico di `wands.yml` con copia da resources
- ✨ **Nuovo:** Metodo `loadWandsConfig()` per gestire file custom
- ✨ **Nuovo:** Getter `getWandsConfig()` per accesso configurazione wand
- 🔧 **Migliorato:** Separazione logica tra config generale e definizioni wand
- 🔧 **Migliorato:** `config.yml` ora contiene solo impostazioni generali
- 🔧 **Ottimizzato:** Gestione file YAML con YamlConfiguration
- 📚 **Documentazione:** Aggiornata sezione configurazione con file separati
- 📚 **Documentazione:** Chiarito uso di config.yml vs wands.yml
- 🎨 **Pulizia:** Rimossa sezione `wands:` da config.yml
- 🎨 **Organizzazione:** Migliore struttura file per manutenzione

### **Versione 1.5.0** ↩️
- ✨ **MAJOR:** Sistema di undo configurabile per singola bacchetta
- ✨ **Nuovo:** Opzione `enable-undo` per ogni bacchetta nel config.yml
- ✨ **Nuovo:** Possibilità di abilitare/disabilitare l'annullamento con click sinistro per ogni bacchetta
- 🔧 **Migliorato:** Controllo undo specifico per bacchetta invece che globale
- 📚 **Documentazione:** Aggiornata sezione configurazione con parametro `enable-undo`
- 📚 **Documentazione:** Aggiunto parametro nella lista funzionalità principali

### **Versione 1.4.0** 🚫
- ✨ **MAJOR:** Sistema di blocchi protetti configurabili
- ✨ **Nuovo:** Opzione `blocked-blocks` nel config.yml per impedire l'uso su blocchi specifici
- ✨ **Nuovo:** Validazione automatica dei blocchi con log degli errori
- ✨ **Nuovo:** Messaggio personalizzato `blocked-block` quando si clicca su un blocco protetto
- 🔧 **Migliorato:** Protezione aggiuntiva per blocchi critici come spawner, bedrock, command block
- 🔧 **Ottimizzato:** Controllo blocchi protetti prima del piazzamento
- 📚 **Documentazione:** Nuova sezione "Blocchi Protetti" nel README
- 🐛 **Risolto:** Possibilità di usare bacchette su blocchi sensibili del server

### **Versione 1.2.0** 🎯
- ✨ **MAJOR:** Integrazione completa con SuperiorSkyblock2
- ✨ **Nuovo:** Supporto per coop players e permessi personalizzati isole
- ✨ **Nuovo:** Bypass admin per staffer con `/is admin bypass`
- ✨ **Nuovo:** Messaggio di errore quando non si ha il permesso per costruire su un'isola
- ✨ **Nuovo:** Controllo world border (sempre attivo, anche con bypass admin)
- ✨ **Nuovo:** Compatibilità con altri plugin di protezione tramite BlockPlaceEvent
- ✨ **Nuovo:** Sistema di reflection per compatibilità con diverse versioni API SS2
- 🔧 **Migliorato:** Gestione protezioni con classe dedicata `Protections.java`
- 🔧 **Migliorato:** Logging errori API con comportamento permissivo in caso di mismatch versione
- 🔧 **Ottimizzato:** Controlli di sicurezza eseguiti prima di ogni piazzamento blocco
- 📚 **Documentazione:** Aggiunto supporto softdepend in plugin.yml
- 🐛 **Risolto:** Blocchi piazzati fuori dal world border con bypass admin

### **Versione 1.1.0**
- ✨ **Nuovo:** Opzione `source` per scegliere da dove prendere i blocchi (offhand/inventory)
- ✨ **Nuovo:** Modalità inventory: piazza blocchi dello stesso tipo di quello cliccato
- ✨ **Nuovo:** Opzione `length` per controllare l'altezza/spessore della griglia
- ✨ **Nuovo:** Sistema di cooldown configurabile con opzione `delay`
- ✨ **Nuovo:** Protezione anti-spam: previene doppio click entro 100ms
- ✨ **Nuovo:** UUID univoci per ogni bacchetta (non stackabili)
- ✨ **Nuovo:** Comando supporta nome player e quantità (anche da console)
- ✨ **Nuovo:** Tab completion per nomi giocatori online e quantità suggerite
- ✨ **Nuovo:** Messaggio personalizzato per mancanza blocchi in inventory
- 🔧 **Migliorato:** Sistema di rimozione blocchi dall'inventario ottimizzato
- 🔧 **Migliorato:** Gestione blocchi piazzati limitata alla quantità disponibile
- 🔧 **Migliorato:** Comandi eseguibili anche da console con specifica del player
- 🔧 **Migliorato:** Notifica al sender quando dà bacchette ad altri giocatori
- 🔧 **Ottimizzato:** Controllo quantità disponibile prima del piazzamento
- 🔧 **Ottimizzato:** Gestione cooldown per giocatore con Map dedicata
- 📚 **Documentazione:** Usage aggiornato in plugin.yml
- 🐛 **Risolto:** Piazzamento di un solo blocco invece della griglia completa
- 🐛 **Risolto:** Messaggio cooldown al primo click
- 🐛 **Risolto:** Doppio click processato come due azioni separate

### **Versione 1.0.0**
- ✨ **Nuovo:** Sistema di bacchette personalizzabili
- ✨ **Nuovo:** Configurazione tramite config.yml
- ✨ **Nuovo:** Range configurabile per griglie di piazzamento
- ✨ **Nuovo:** Sistema di usi limitati o infiniti
- ✨ **Nuovo:** Custom model data per texture pack
- ✨ **Nuovo:** Lore dinamico con placeholder {uses}
- ✨ **Nuovo:** Comando /wand per ottenere bacchette
- ✨ **Nuovo:** Sistema di permessi (give e use)
- ✨ **Nuovo:** Controlli di validità blocchi piazzabili
- ✨ **Nuovo:** Supporto modalità creativa (blocchi infiniti)
- ✨ **Nuovo:** Messaggi configurabili per errori e successi
- ✨ **Nuovo:** Tab completion per nomi bacchette
- ✨ **Nuovo:** Persistenza usi rimanenti tramite PersistentDataContainer
- 🔧 **Implementato:** Logica piazzamento blocchi su faccia cliccata
- 🔧 **Implementato:** Calcolo facce perpendicolari per griglie corrette
- 🔧 **Implementato:** Gestione decremento usi e distruzione bacchetta esaurita
- 📚 **Documentazione:** README iniziale
- 📚 **Documentazione:** Commenti nel config.yml

## 👨‍💻 Sviluppo

### Build
```bash
mvn clean package
```

Il file `.jar` compilato sarà in `target/ConstructionWands-1.0-SNAPSHOT.jar`

### Struttura Progetto
```
src/main/
├── java/com/franchino961/constructionwands/
│   ├── ConstructionWands.java          # Main class del plugin
│   ├── commands/
│   │   └── WandCommand.java            # Comando /wand
│   ├── hooks/
│   │   └── Protections.java            # Hook SS2 e protezioni
│   ├── listeners/
│   │   └── WandInteractListener.java   # Evento click bacchetta
│   ├── managers/
│   │   └── WandManager.java            # Gestione bacchette
│   └── models/
│       └── Wand.java                   # Modello dati bacchetta
└── resources/
    ├── config.yml                       # Configurazione generale
    ├── wands.yml                        # Definizioni bacchette
    └── plugin.yml                       # Metadata plugin
```

## 📄 Licenza

Questo progetto è distribuito sotto licenza MIT.

## 🐛 Segnalazione Bug

Per segnalare bug o richiedere funzionalità, apri una issue su GitHub.

## 💡 Contributi

I contributi sono benvenuti! Sentiti libero di aprire pull request.

---

**Autore**: Franchino961  
**Versione**: 1.7.0  
**API**: Spigot 1.20.1  
**Java**: 17
