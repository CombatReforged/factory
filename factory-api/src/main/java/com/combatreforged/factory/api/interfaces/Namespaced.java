package com.combatreforged.factory.api.interfaces;

import com.combatreforged.factory.api.util.Identifier;
import com.combatreforged.factory.api.util.ImplementationUtils;

public interface Namespaced {
    default Identifier getNamespaceId() {
        return ImplementationUtils.getInstance().getIdentifier(this);
    }
}
