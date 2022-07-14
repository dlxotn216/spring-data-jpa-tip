package me.taesu.springdatajpaquerydsltip.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class StringListConverter(private val objectMapper: ObjectMapper):
    AttributeConverter<List<String>, String> {
    override fun convertToEntityAttribute(jsonString: String?): List<String> {
        if (jsonString.isNullOrBlank()) {
            return emptyList()
        }
        return objectMapper.readValue(jsonString, object: TypeReference<List<String>>() {})
    }

    override fun convertToDatabaseColumn(attribute: List<String>?): String {
        attribute ?: return "[]"
        return objectMapper.writeValueAsString(attribute)
    }
}