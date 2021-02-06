package org.labmonkeys.home_library.catalog.api;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.labmonkeys.home_library.catalog.BookCatalogException;
import org.labmonkeys.home_library.catalog.aop.Audited;
import org.labmonkeys.home_library.catalog.colaborators.open_library.api.OpenLibrary;
import org.labmonkeys.home_library.catalog.dto.BookInfoDTO;
import org.labmonkeys.home_library.catalog.mapper.BookInfoMapper;
import org.labmonkeys.home_library.catalog.model.BookInfo;
import org.labmonkeys.home_library.catalog.model.ISBN;

@Path("/bookCatalog")
@ApplicationScoped
public class BookCatalogService {

    @Inject
    private BookInfoMapper bookInfoMapper;

    @Inject
    @RestClient
    private OpenLibrary openLibrary;

    @GET
    @Path("/getBookInfo/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    @Audited
    public BookInfoDTO getBookInfo(@PathParam("isbn") String isbn) throws BookCatalogException {

        final Logger LOG = Logger.getLogger(BookCatalogService.class);
        LOG.info("getBookInfoByIsbn method invoked!");
        BookInfoDTO bookInfoDto = null;
        BookInfo bookInfoEntity = null;
        bookInfoEntity = ISBN.getBookInfoByIsbn(isbn);
        if (bookInfoEntity == null) {
            isbn = "ISBN:" + isbn;
            bookInfoDto = bookInfoMapper.bookInfoOlToBookInfoDTO(openLibrary.getBookInfo(isbn, "json", "data"));
            bookInfoDto.setInCatalog(false);
        } else {
            bookInfoDto = bookInfoMapper.bookInfoEntityToDto(bookInfoEntity);
            bookInfoDto.setInCatalog(true);
        }

        return bookInfoDto;
    }

    @POST
    @Path("/saveBookInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void saveBookInfo(BookInfoDTO dto) throws BookCatalogException {
        BookInfo bookInfoEntity = bookInfoMapper.bookInfoDtoToEntity(dto);
        BookInfo.persist(bookInfoEntity);
    }
}