package com.cryptnation;

import picocli.CommandLine.Option;

import java.io.File;

class ExclusiveHashOptions {
    @Option(names = {"-f", "--file"}, paramLabel = "FILE", description = "file to be hashed")
    File file;

    @Option(names = {"-s", "--string"}, paramLabel = "STRING", description = "String to be hashed")
    String str;
}
