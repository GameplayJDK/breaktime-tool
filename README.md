# breaktime-tool

## Installation

To set up the tool:

- Download the latest release.
- Place a configuration file in the same directory as the jar file.
- Make sure the configuration is valid. Invalid configuration can be diagnosed by getting an empty window.

More information can be found in `example_configuration.properties`.

## Feature

The tool supports loading a time configuration using regex, distinguishing between two types of labels to display:

- Labels containing text only.
- Labels containing text and time.

Details can be found in the `Configuration` class.

Another feature is a small hack regarding window transparency while maintaining the platform style, which would not be
possible by default. Details can be found in the `Main` class.

Colorization of past, current and upcoming labels is supported. The details can be found inside the `TimeLabel` class.

## About

The project was a 2.5 year-long part-time experiment, fueled by the rather boring school lessons.

I began programming the tool when I started my apprenticeship and worked on it in school.

This final version, for historical reasons, is tagged v3.4, as it is the 4th iteration of the 3rd codebase.

## License

MIT license. For details see the LICENSE.txt file.
