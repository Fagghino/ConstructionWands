# Changelog - ConstructionWands

Tutte le modifiche rilevanti al plugin **ConstructionWands** saranno documentate in questo file.

Il formato è basato su [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
e questo progetto segue il [Versionamento Semantico](https://semver.org/spec/v2.0.0.html).

---

## [1.7.0] - 2026-06-15

### Aggiunto
- **Modalità di Piazzamento**: Sistema di modalità di piazzamento configurabili — Tre modalità: `AUTO`, `VERTICALE`, `ORIZZONTALE`.
- **BlockPlaceEvent**: Aggiunto dispatch di `BlockPlaceEvent` per compatibilità completa con SuperiorSkyblock2.
- **Left Click Action**: Opzione configurabile `left-click-action` (`NONE` / `UNDO` / `MODE`).
- **Cambio Modalità**: Cambio modalità con click sinistro quando `left-click-action: MODE`.
- **Persistenza Modalità**: Persistenza modalità per ogni player e bacchetta individualmente.
- **Messaggi Modalità**: Messaggi configurabili per cambio modalità (`mode-auto`, `mode-vertical`, `mode-horizontal`).

### Modificato
- **SuperiorSkyblock2**: I blocchi piazzati vengono ora conteggiati correttamente per il livello isola SS2.
- **Left Click**: Sistema left-click unificato con switch-case.
- **Logica Piazzamento**: Il calcolo della direzione di piazzamento è ora basato sulla modalità selezionata.

### Risolto
- **Livello SS2**: Blocchi piazzati con bacchetta non conteggiati nel livello isola SS2.
- **BlockPlaceEvent**: `BlockPlaceEvent` non triggerato correttamente.

### Rimosso
- **enable-undo**: Parametro `enable-undo` rimosso (sostituito da `left-click-action`).

---

## [1.6.0] - 2026-05-15

### Aggiunto
- **File Config Separati**: File `wands.yml` dedicato per le definizioni delle bacchette.
- **Auto-Caricamento**: Caricamento automatico di `wands.yml` da resources.
- **API**: Aggiunto metodo `loadWandsConfig()` e getter `getWandsConfig()`.

### Modificato
- **config.yml**: Ora contiene solo le impostazioni generali, separato dalle definizioni bacchette.

---

## [1.5.0] - 2026-04-15

### Aggiunto
- **Undo per Bacchetta**: Sistema di undo configurabile per singola bacchetta tramite opzione `enable-undo` in `config.yml`.

---

## [1.4.0] - 2026-03-15

### Aggiunto
- **Blocchi Protetti**: Sistema di blocchi protetti configurabili tramite opzione `blocked-blocks`.
- **Validazione**: Validazione automatica dei blocchi con log degli errori.
- **Messaggi**: Messaggio `blocked-block` configurabile al click su blocco protetto.

### Risolto
- **Sicurezza**: Impedito l'uso delle bacchette su blocchi sensibili (spawner, bedrock, command block).

---

## [1.2.0] - 2026-02-15

### Aggiunto
- **Integrazione SuperiorSkyblock2**: Integrazione completa con SuperiorSkyblock2.
- **Supporto Coop**: Supporto per coop players e permessi personalizzati isole.
- **Bypass Admin**: Bypass admin (`/is admin bypass`).
- **Permesso Costruzione**: Messaggio errore quando non si ha il permesso di costruire su un'isola.
- **World Border**: Controllo world border (sempre attivo, anche con bypass admin).
- **Compatibilità Plugin**: Compatibilità con altri plugin di protezione tramite `BlockPlaceEvent`.
- **Sistema Reflection**: Sistema di reflection per compatibilità versioni API SS2.
- **Classe Protections**: Classe dedicata `Protections.java`.

### Risolto
- **World Border**: Blocchi piazzati fuori dal world border con bypass admin.

---

## [1.1.0] - 2026-01-15

### Aggiunto
- **Sorgente Blocchi**: Opzione `source` per scegliere da dove prendere i blocchi (offhand/inventory).
- **Modalità Inventory**: Piazza blocchi dello stesso tipo di quello cliccato.
- **Opzione Length**: Opzione `length` per controllare altezza/spessore della griglia.
- **Cooldown**: Sistema di cooldown configurabile con opzione `delay`.
- **Anti-Spam**: Protezione anti-spam — previene doppio click entro 100ms.
- **UUID Univoci**: UUID univoci per ogni bacchetta (non stackabili).
- **Supporto Console**: Il comando supporta nome player e quantità anche dalla console.
- **Tab Completion**: Tab completion per nomi giocatori online e quantità suggerite.
- **Messaggio Mancanza Blocchi**: Messaggio personalizzato per mancanza blocchi in inventario.

### Risolto
- **Griglia**: Piazzamento di un solo blocco invece della griglia completa.
- **Messaggio Cooldown**: Messaggio cooldown al primo click.
- **Doppio Click**: Doppio click processato come due azioni separate.

---

## [1.0.0] - 2025-12-15

### Aggiunto
- **Prima Release**: Prima versione di ConstructionWands.
- **Sistema Bacchette**: Sistema di bacchette personalizzabili tramite `config.yml`.
- **Range**: Range configurabile per griglie di piazzamento.
- **Sistema Usi**: Sistema di usi limitati o infiniti.
- **Custom Model Data**: Supporto custom model data per texture pack.
- **Lore Dinamico**: Lore dinamico con placeholder `{uses}`.
- **Comandi**: Comando `/wand` per ottenere bacchette.
- **Permessi**: Sistema di permessi (give e use).
- **Validazione Blocchi**: Controlli di validità blocchi piazzabili.
- **Modalità Creativa**: Supporto modalità creativa (blocchi infiniti).
- **Messaggi**: Messaggi configurabili per errori e successi.
- **Tab Completion**: Tab completion per nomi bacchette.
- **Persistenza**: Persistenza usi tramite `PersistentDataContainer`.

---

## Roadmap di Sviluppo

### Fase 1 - Prima Release ✅
- Sistema base di bacchette con range e usi configurabili.

### Fase 2 - Usabilità ✅
- Anti-spam, cooldown, modalità inventory, tab completion.

### Fase 3 - Integrazioni ✅
- Integrazione SuperiorSkyblock2 con permessi e supporto isole.

### Fase 4 - Configurazione ✅
- Blocchi protetti, file config separati, undo per bacchetta.

### Fase 5 - Modalità Piazzamento ✅
- Modalità AUTO/VERTICALE/ORIZZONTALE.

### Fase 6 - Funzionalità Avanzate 📋
- Supporto forme multi-blocco.
- Cronologia undo con livelli multipli.
- Ricette di crafting per bacchette.

---

*Formato: [Versione] - Data*
*Categorie: Aggiunto, Modificato, Risolto, Rimosso*
