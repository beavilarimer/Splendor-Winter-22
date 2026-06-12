# Splendor — Java Implementation

A from-scratch implementation of the board game [Splendor](https://boardgamegeek.com/boardgame/148228/splendor) in Java, built as an OOP practice project. Supports 2–4 players with full rules.

## Project status

- [x] Model layer (Phase 1) — complete
- [ ] Game logic (Phase 2) — in progress
- [ ] JavaFX GUI (Phase 3) — planned

## Model layer

| Class | Responsibility |
|---|---|
| `Card` | Gem color, chip cost, prestige points, reserved flag. Maps color string → index on construction. |
| `Deck` | Array-backed deck with Fisher-Yates shuffle, draw (`removeCard`), and non-destructive peek (`peekCard`). |
| `Noble` | Card requirements to trigger a noble visit. Awards `Noble.PRESTIGE` (3) points. |
| `Player` | Chip bank (6 slots: 5 colors + gold), card tracker (5 colors), prestige, and one reserved card slot. |
| `Splendor` | Game state: 3 tiered decks (40/30/20 cards), chip bank, nobles, and the 4×3 face-up table. |

## Rules summary

- On your turn, do exactly one of: take 3 different-color chips, take 2 same-color chips (4+ in bank), buy a card, or reserve a card.
- Buying a card uses chips + gold wildcards; owned cards of the matching color act as permanent discounts.
- After buying, nobles auto-visit if their card-color requirements are met.
- First player to reach 15 prestige triggers the final round; most prestige wins (tiebreaker: fewest cards purchased).

## References

Card costs sourced from [jonkchan/Splendor-Java](https://github.com/jonkchan/Splendor-Java).
