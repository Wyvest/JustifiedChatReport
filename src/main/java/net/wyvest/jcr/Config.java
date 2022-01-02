package net.wyvest.jcr;

import java.util.LinkedHashSet;

@SuppressWarnings("all")
public class Config {
    public boolean enabled = true;
    public int confirmSeconds = 5;

    public LinkedHashSet<String> startsWith = new LinkedHashSet<>();
    public LinkedHashSet<String> endsWith = new LinkedHashSet<>();
    public LinkedHashSet<String> contains = new LinkedHashSet<>();
    public LinkedHashSet<String> equals = new LinkedHashSet<>();
    public LinkedHashSet<String> startsWithWords = new LinkedHashSet<>();
    public LinkedHashSet<String> endsWithWords = new LinkedHashSet<>();
    public LinkedHashSet<String> containsWords = new LinkedHashSet<>();
    public LinkedHashSet<String> equalsWords = new LinkedHashSet<>();
}
