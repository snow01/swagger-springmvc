package com.mangofactory.swagger.models

import com.mangofactory.swagger.mixins.ModelProviderSupport
import com.mangofactory.swagger.mixins.TypesForTestingSupport
import spock.lang.Specification

@Mixin([TypesForTestingSupport, ModelProviderSupport])
class ModelDependencyProviderSpec extends Specification {
  def "dependencies are inferred correctly" () {
    given:
      ModelDependencyProvider provider = defaultModelDependencyProvider()
      def context = ModelContext.inputParam(modelType)
      def dependentTypes = provider.dependentModels(context)
      def dependentTypeNames = dependentTypes.collect() { ResolvedTypes.typeName(it) }.unique().sort()
    expect:
     dependencies == dependentTypeNames

    where:
    modelType                       | dependencies
    simpleType()                    | []
    complexType()                   | ["com.mangofactory.swagger.models.Category"]
    inheritedComplexType()          | ["com.mangofactory.swagger.models.Category"]
    typeWithLists()                 | ["List", "com.mangofactory.swagger.models.Category",  "com.mangofactory.swagger.models.ComplexType"].sort()
    typeWithSets()                  | ["Set", "com.mangofactory.swagger.models.Category",  "com.mangofactory.swagger.models.ComplexType"].sort()
    typeWithArrays()                | ["Array", "com.mangofactory.swagger.models.Category", "com.mangofactory.swagger.models.ComplexType"]
    genericClass()                  | ["List", "com.mangofactory.swagger.models.SimpleType"]
    genericClassWithListField()     | ["List", "com.mangofactory.swagger.models.SimpleType"]
    genericClassWithGenericField()  | ["java.nio.charset.Charset", "org.springframework.http.HttpHeaders", "List",  "Map«string,string»", "org.springframework.http.MediaType", "ResponseEntity«com.mangofactory.swagger.models.SimpleType»", "Set", "com.mangofactory.swagger.models.SimpleType", "java.net.URI"].sort()
    genericClassWithDeepGenerics()  | ["java.nio.charset.Charset", "org.springframework.http.HttpHeaders", "List",  "Map«string,string»", "org.springframework.http.MediaType", "ResponseEntity«List«com.mangofactory.swagger.models.SimpleType»»", "Set", "com.mangofactory.swagger.models.SimpleType", "java.net.URI"].sort()
    genericCollectionWithEnum()     | ["Collection«string»", "List"]
    recursiveType()                 | ["com.mangofactory.swagger.models.SimpleType"]
  }



}
