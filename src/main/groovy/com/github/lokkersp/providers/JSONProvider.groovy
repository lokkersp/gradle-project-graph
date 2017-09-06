package com.github.lokkersp.providers

class JSONProvider implements Provider{
    @Override
    String serialize() {
        return this.toString()
    }
}
