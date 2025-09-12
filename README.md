# EmacsJ-plus

<!-- Plugin description -->
A collection of useful, Emacs-inspired commands for text editing and search.

Documentation: EmacsJ-plus on [GitHub](https://github.com/strindberg/emacsj-plus)

This plugin is a collection of commands adding Emacs-inspired functionality to IntelliJ-based IDEs. It is mainly intended as an
extension to IntelliJ's Emacs keymap, which lacks some functionality that an Emacs user will miss. Moreover, many commands work slightly
differently in IntelliJ and Emacs. EmacsJ's aim is to offer features that bridge that gap so that switching between Emacs and IntelliJ
becomes easy. Many of these commands could also be of interest to non-Emacs users.

There are two main ways of using EmacsJ: the plugin installs a number of commands from which you can either pick-and-choose or
activate all at once by choosing the *EmacsJ* keymap. If you want the former, study the available commands under Settings → Keymap →
Plugins → EmacsJ and activate the ones you find interesting. Alternatively, you can choose the keymap *EmacsJ* under Settings →
Keymap. This keymap is an extension of the standard *Emacs* keymap, and this way all the EmacsJ commands are activated. You can then
deactivate the ones you don't want to use.

The main features are:

- Incremental search modeled on Emacs' Isearch, with text and regexp search.
- Query-replace with text or regexps, with a very light-weight interface.
- Word commands: transpose, upper-case, lower-case, capitalize, move, delete.
- Easy access to clipboard history à la Emacs (kill ring).
- A mark history with the ability to pop mark (mark ring), and exchange point and mark.
- Universal argument: repeat commands a specified number of times.
- Append-next-kill: append copied/cut text to previous kill.
- Rectangle commands: copy, open, clear, paste.
- Zap to character.
- Go back in XRef history.
- Whitespace commands: delete space around point, delete empty lines.
- Duplicate and comment regions and lines.
- Recenter and relocate caret.

Documentation: EmacsJ on [GitHub](https://github.com/strindberg/emacsj)
<!-- Plugin description end -->

