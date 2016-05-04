package com.benine.backend.http;

import java.util.jar.Attributes;

/**
 * Created by dorian on 4-5-16.
 */
public class URIparser {
  /**
   * Decodes the given (decoded) uri into an attributes table
   * @param uri the uri to parse.
   * @return an attributes object containing key->value(string->string)
   * pairs for the uri parameters.
   */
  public static Attributes parseURI(String uri) throws MalformedURIException {
    Attributes params = new Attributes();
    for(String pair : uri.split("&")) {
      String[] splitPair = pair.split("=");
      if(params.containsKey(new Attributes.Name(splitPair[0]))) {
        throw new MalformedURIException("Multiple occurences of parameter with name: " + splitPair[0]);
      }
      params.putValue(splitPair[0], splitPair[1]);
    }

    return params;
  }


}
