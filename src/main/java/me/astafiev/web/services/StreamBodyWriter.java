/*
 * Авторское право принадлежит Антону Александровичу Астафьеву <anton@astafiev.me> (Anton Astafiev) ѱ.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Антон Александрович Астафьев <anton@astafiev.me> (Anton Astafiev). All rights reserved.
 * 
 *  Собственная лицензия Астафьева
 * Данный программный код является собственностью Астафьева Антона Александровича
 * и может быть использован только с его личного разрешения
 */

package me.astafiev.web.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
@Provider
@Produces({MediaType.APPLICATION_JSON})
public class StreamBodyWriter implements MessageBodyWriter {

	@Override
	public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Stream.class.isAssignableFrom(type) && MediaType.APPLICATION_JSON_TYPE.equals(mediaType);
	}

	@Override
	public long getSize(Object t, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Object t, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		Stream stream = (Stream) t;
		Jsonb jb = JsonbBuilder.newBuilder().build();
//		JsonObjectBuilder job = Json.createObjectBuilder();
		RSWriter writer = new RSWriter(entityStream);
		JsonGenerator jg = Json.createGenerator(writer);
		jg.writeStartArray();
		jg.flush();
		stream.forEach(e -> writeEl(writer, jb, e));
		jg.writeEnd();
		jg.flush();
		writer.unblock();
	}
	
	private void writeEl(RSWriter writer, Jsonb jb, Object el) {
		try {
			if (writer.started()) {
				writer.append(',');
			}
			jb.toJson(el, writer);
			writer.flush();
		} catch (IOException ex) {
			Logger.getLogger(StreamBodyWriter.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private class RSWriter extends OutputStreamWriter {
		public RSWriter(OutputStream out) {
			super(out);
		}

		private boolean blockClose = true;
		private boolean firstEl = true;

		@Override
		public void close() throws IOException {
			if (blockClose) return;
			super.close();
		}

		void unblock() {
			blockClose = false;
		}
		boolean started() {
			if (firstEl) {
				firstEl = false;
				return false;
			}
			return true;
		}
	}

}
