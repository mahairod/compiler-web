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

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import me.astafiev.web.compiler.beans.Completion;
import me.astafiev.web.compiler.beans.CompletionType;
import me.astafiev.web.compiler.state.CompileStateLocal;
import me.astafiev.web.compiler.state.IO;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
@Stateless
@Path("actions")
public class ПримесьДействиеFacadeREST {
	@EJB
	private CompileStateLocal compileState;

	@GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
	public String find(@PathParam("id") Long id) {
		return "Probe";
	}

	@GET
    @Produces({"application/xml", "application/json"})
	public List<Completion> findAll() {
		return Lists.newArrayList(new Completion(CompletionType.MEMEBER, "SingleItem"));
	}

	@GET
    @Path("src/{klass}")
    @Produces({"application/xml"})
	public List<Completion> findCompletions(
			@PathParam("klass") String klass,
			@QueryParam("prefix") String prefix,
			@QueryParam("line") int line,
			@QueryParam("col") int col) {
		return Lists.newArrayList(new Completion(CompletionType.MEMEBER, "SingleItem"));
	}

	@GET
    @Path("completions/{klass}")
    @Produces({"application/json"})
	public Stream<Completion> findCompletionsJson(
			@PathParam("klass") String klass,
			@QueryParam("prefix") String prefix,
			@QueryParam("line") int line,
			@QueryParam("col") int col) throws IOException {
		compileState.saveSource(klass, IO.readSample());
		return compileState.getCompletions(klass, prefix, col, col);
	}

	@POST
    @Path("initcomplete/{klass}")
    @Produces({"application/json"})
	public Stream<Completion> findNewCompletionsJson(
			@PathParam("klass") String klass,
			@QueryParam("prefix") String prefix,
			@QueryParam("line") int line,
			@QueryParam("col") int col,
			String source
			) {
		compileState.saveSource(klass, source);
		return compileState.getCompletions(klass, prefix, col, col);
	}

	@GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
	public List<String> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
		return Lists.newArrayList("SingleItem");
	}

	@GET
    @Path("count")
    @Produces("text/plain")
	public String countREST() {
		return "7";
	}

}
