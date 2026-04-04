# 🏗️ ConstructionWands

[![Version](https://img.shields.io/badge/version-1.7.0-blue.svg)]()
[![Minecraft](https://img.shields.io/badge/minecraft-1.20+-green.svg)](https://www.minecraft.net/)
[![License](https://img.shields.io/badge/license-MIT-yellow.svg)](../LICENSE)
[![Spigot](https://img.shields.io/badge/Spigot-1.20+-orange.svg)](https://www.spigotmc.org/)

[![en](https://img.shields.io/badge/lang-en-red.svg)](../README.md)
[![it](https://img.shields.io/badge/lang-it-green.svg)](README.it.md)

> 📝 [Changelog](CHANGELOG.it.md)

**ConstructionWands** è un plugin Spigot/Paper per bacchette di costruzione personalizzate con supporto completo per SuperiorSkyblock2. Permette di creare bacchette per piazzare blocchi in griglie di dimensioni configurabili con controllo granulare delle protezioni.

## 📋 Descrizione

ConstructionWands è un plugin Minecraft che permette di creare bacchette personalizzate per piazzare blocchi in griglia. Supporta configurazione avanzata per range, altezza, cooldown, e integrazione completa con SuperiorSkyblock2 per la gestione delle protezioni delle isole.

## ✨ Caratteristiche

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

## 📦 Requisiti

- **Server**: Spigot/Paper 1.20.1+
- **Java**: 17+
- **Dipendenze opzionali**:
  - SuperiorSkyblock2 (per protezione isole)

## 🚀 Installazione

1. Scarica l'ultima versione del plugin
2. Copia il file `.jar` nella cartella `plugins` del server
3. (Opzionale) Installa SuperiorSkyblock2 per il supporto isole
4. Avvia/riavvia il server
5. Modifica `config.yml` e `wands.yml` per personalizzare le bacchette

## ⚙️ Configurazione

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

## 👨‍💻 Struttura Progetto
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

Questo progetto è rilasciato sotto licenza **MIT** — vedi il file [LICENSE](../LICENSE) per i dettagli.

## 👤 Autore

**Franchino961** — [GitHub](https://github.com/Franchino961-Plugins)

## 🤝 Contributi

I contributi sono benvenuti!
- 🐛 Segnala bug nelle [issues](../../issues)
- 💡 Proponi nuove funzionalità
- 🔧 Invia Pull Request

## 💬 Supporto

Per bug report, richieste di funzionalità o domande:
- Apri una [issue](../../issues) su GitHub
- Contatta lo sviluppatore

## 🔗 Link Utili

- 📚 [Spigot API Documentation](https://hub.spigotmc.org/javadocs/spigot/)
- 🏙️ [SuperiorSkyblock2 Wiki](https://wiki.bg-software.com/superiorskyblock2/)

## 📝 Changelog

Consulta [CHANGELOG.it.md](MD/CHANGELOG.it.md) per la cronologia completa delle versioni.