About this file
---------------
Bored? Have a look at this file for inspirations about refactoring opportunities.

Discovered something really ugly, that others should refactor? Or maybe even yourself, but not now?
Take a note.

We don't need no (technical) debt

Eliminate Node
--------------
Nodes are awful. They were once designed to support nesting all kinds of stuff (subjects, actions, modifiers, observations),
but we support only groups of action now. Since we plan to keep it this way, we don't have a need for this awful,
non-generic Node stuff, but can instead introduce an ActionGroup class instead.

This means:
- deleting Node.class
- deleting NodeDaoImpl
- changing Study.class to return appropriate entities
- fixing UI where needed

Don't recreate the tree
-----------------------
Currently, we are deleting the tree for the project setup on each change. While this is no big problem performance-wise,
settings like which node is collapsed and which isn't are lost - everything will get expanded.
Before we store this collapse-information somewhere, we should only update the tree where needed.

Generify / Subclass ModifierFactory
-----------------------------------
Since OrmLite doesn't support mapping of inheritance structures, ModifierFactory.class is designed to support
EVERY possible ModifierFactory.

Debts without fancy description
-------------------------------
- refactor context menu handling. It's just ugly.