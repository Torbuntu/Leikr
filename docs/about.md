# About Leikr
| [Wiki](https://github.com/torbuntu/leikr/wiki) | [itch.io](https://torbuntu.itch.io/leikr) | [Releases](https://github.com/torbuntu/leikr/releases) | [Home](https://torbuntu.github.io/Leikr/) |


## Origin

The word Leikr is Old Norse for [Game](https://en.wiktionary.org/wiki/leikr). It is a play on the name Lego, from the Danish ["Leg Godt"](https://en.wikipedia.org/wiki/Lego), meaning "Play Well". 

Leikr Game System was originally built to teach myself [Groovy](https://groovy-lang.org/) and game development in a smaller environment than more robust game frameworks.

## Goals

Leikr aims to be a platform for unleashing creativity in an easy to use format with a simple project structure and easy to learn  [API](https://github.com/torbuntu/leikr/wiki/api) in the [Groovy](http://groovy-lang.org/) programming language.
Wether you are just starting out in programming, or are a professional, Leikr is a great system for easily prototyping and building out games and programs. Since under the hood Leikr is built in Java, it can run on the major Operating Systems, but also designed to run on ARM systems like the Raspberry Pi 3B, 3B+ and 3A+, and ClockworkPi GameShell.

The primary objectives of the Leikr system
1. Help teach Groovy, Java and the JVM.
2. Provide a platform for making Games and Programs with similar look and feel to Gameboy Advance era games.
3. Provide a path to migrate to larger game frameworks ([Mini2Dx](https://mini2dx.org/), [LibGDx](https://libgdx.badlogicgames.com/))

## NON-Goals

It is **not** the intention of the Leikr Game System to compete or replace any current Fantasy Consoles. The specific needs and space of Leikr, in my opinion, are not the same. I highly recommend exploring other systems for different needs. If your goals align with the above list, then Leikr is for you.

## Tools

Most Fantasy Console systems typically come with all or some of the tools needed for making games baked in, ready to go out of the box. Since the goals of Leikr do not include teaching how to use new tools, they are not in the initial scope of the platform. 

Instead, Leikr aims to promote the use of whatever tools someone is most comfortable with. For example, I personally use the following:

| Type | Tool | Note |
|----|----|-----|
| Art, Sprites | [Aseprite](https://www.aseprite.org/) / [LibreSprite](https://github.com/LibreSprite/LibreSprite), [Gimp](https://www.gimp.org/) | for sprites Leikr needs a file called Sprites.png, but in the art directory any png, jpg, or bmp file will work |
| Music | [LMMS](https://lmms.io/), [FL Studio](https://image-line.com) | Leikr uses .WAV, .mp3, and .ogg  files for Music |
| Sfx | [RFXGEN](https://github.com/raysan5/rfxgen) | Leikr uses .WAV, .mp3, and .ogg files for SFX |
| Code | [NetBeans](https://netbeans.apache.org/), [Gedit](https://wiki.gnome.org/Apps/Gedit), [Nano](https://www.nano-editor.org/)| Any Text editor with support for Groovy highlighting will be plenty sufficient |

Using simple to make assets allows for users to use any tools they wish for developing on Leikr[.](https://torbuntu.github.io/Leikr/docs/hacking) 
