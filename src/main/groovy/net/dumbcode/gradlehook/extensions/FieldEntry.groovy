package net.dumbcode.gradlehook.extensions

import groovy.transform.TupleConstructor

/**
 * Used to hold information about the field entry that is stored
 */
@TupleConstructor
class FieldEntry {
    String name
    String value
}
