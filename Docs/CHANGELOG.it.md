# Changelog

Tutte le modifiche rilevanti a questo progetto saranno documentate in questo file.

Il formato è basato su [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
e questo progetto segue il [Versionamento Semantico](https://semver.org/spec/v2.0.0.html).

## [1.7.0] - Corrente — Modalità di Piazzamento Configurabili

### ✨ Aggiunto
- **MAJOR:** Sistema di modalità di piazzamento configurabili
- **MAJOR:** `BlockPlaceEvent` per compatibilità completa con SuperiorSkyblock2
- Opzione `left-click-action` configurabile (`NONE` / `UNDO` / `MODE`)
- Tre modalità di piazzamento: `AUTO`, `VERTICALE`, `ORIZZONTALE`
- Cambio modalità con click sinistro quando `left-click-action: MODE`
- Persistenza modalità per ogni player e bacchetta individualmente
- Messaggi configurabili per cambio modalità (`mode-auto`, `mode-vertical`, `mode-horizontal`)

### 🔧 Migliorato
- Blocchi piazzati triggerano `BlockPlaceEvent` per SS2 e altri plugin
- SuperiorSkyblock2 ora conta correttamente i blocchi per il livello isola
- Sistema left-click unificato con switch case
- Calcolo direzione piazzamento basato su modalità selezionata

### 🐛 Risolto
- Blocchi piazzati con wand non conteggiati nel livello isola SS2
- Eventi `BlockPlaceEvent` non triggerati correttamente

### 🗑️ Rimosso
- Parametro `enable-undo` (sostituito da `left-click-action`)

## [1.6.0] — File di Configurazione Separati

### ✨ Aggiunto
- **MAJOR:** Sistema di configurazione file separati
- File `wands.yml` dedicato per le definizioni bacchette
- Caricamento automatico di `wands.yml` da resources
- Metodo `loadWandsConfig()` e getter `getWandsConfig()`

### 🔧 Migliorato
- Separazione tra configurazione generale e definizioni bacchette
- `config.yml` ora contiene solo impostazioni generali

## [1.5.0] — Undo Configurabile per Bacchetta

### ✨ Aggiunto
- **MAJOR:** Sistema di undo configurabile per singola bacchetta
- Opzione `enable-undo` per ogni bacchetta in `config.yml`

## [1.4.0] — Sistema Blocchi Protetti

### ✨ Aggiunto
- Sistema di blocchi protetti configurabili
- Opzione `blocked-blocks` in `config.yml`
- Validazione automatica blocchi con log degli errori
- Messaggio `blocked-block` configurabile al click su blocco protetto

### 🐛 Risolto
- Possibilità di usare bacchette su blocchi sensibili (spawner, bedrock, command block)

## [1.2.0] — Integrazione SuperiorSkyblock2

### ✨ Aggiunto
- Integrazione completa con SuperiorSkyblock2
- Supporto per coop players e permessi personalizzati isole
- Bypass admin (`/is admin bypass`)
- Messaggio errore quando non si ha il permesso di costruire su un'isola
- Controllo world border (sempre attivo, anche con bypass admin)
- Compatibilità con altri plugin di protezione tramite `BlockPlaceEvent`
- Sistema di reflection per compatibilità versioni API SS2
- Classe dedicata `Protections.java`

### 🐛 Risolto
- Blocchi piazzati fuori dal world border con bypass admin

## [1.1.0]

### ✨ Aggiunto
- Opzione `source` per scegliere da dove prendere i blocchi (offhand/inventory)
- Modalità inventory: piazza blocchi dello stesso tipo di quello cliccato
- Opzione `length` per controllare altezza/spessore della griglia
- Sistema di cooldown configurabile con opzione `delay`
- Protezione anti-spam: previene doppio click entro 100ms
- UUID univoci per ogni bacchetta (non stackabili)
- Comando supporta nome player e quantità (anche da console)
- Tab completion per nomi giocatori online e quantità suggerite
- Messaggio personalizzato per mancanza blocchi

### 🐛 Risolto
- Piazzamento di un solo blocco invece della griglia completa
- Messaggio cooldown al primo click
- Doppio click processato come due azioni separate

## [1.0.0] — Release Iniziale

### Aggiunto
- Sistema di bacchette personalizzabili
- Configurazione tramite `config.yml`
- Range configurabile per griglie di piazzamento
- Sistema di usi limitati o infiniti
- Custom model data per texture pack
- Lore dinamico con placeholder `{uses}`
- Comando `/wand` per ottenere bacchette
- Sistema di permessi (give e use)
- Controlli di validità blocchi piazzabili
- Supporto modalità creativa (blocchi infiniti)
- Messaggi configurabili per errori e successi
- Tab completion per nomi bacchette
- Persistenza usi tramite `PersistentDataContainer`

## [Non Rilasciato]

### Pianificato
- Supporto forme multi-blocco
- Cronologia undo con livelli multipli
- Ricette di crafting per bacchette

---

## Cronologia Versioni

### Come Leggere i Numeri di Versione
- **Major.Minor.Patch** (es. 1.7.0)
  - **Major**: Modifiche incompatibili o aggiunta di funzionalità principali
  - **Minor**: Nuove funzionalità, compatibili con versioni precedenti
  - **Patch**: Correzioni di bug e piccoli miglioramenti

[1.7.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.7.0
[1.6.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.6.0
[1.5.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.5.0
[1.4.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.4.0
[1.2.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.2.0
[1.1.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.1.0
[1.0.0]: https://github.com/franchino961/constructionwands/releases/tag/v1.0.0
