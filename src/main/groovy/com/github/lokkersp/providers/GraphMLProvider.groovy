package com.github.lokkersp.providers

import sun.reflect.generics.reflectiveObjects.NotImplementedException

class GraphMLProvider implements Provider{
    @Override
    String serialize() {
        return this.toString()
    }
}
