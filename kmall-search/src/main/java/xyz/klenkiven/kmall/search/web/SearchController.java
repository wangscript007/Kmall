package xyz.klenkiven.kmall.search.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.klenkiven.kmall.search.service.MallSearchService;
import xyz.klenkiven.kmall.search.vo.SearchParam;
import xyz.klenkiven.kmall.search.vo.SearchResult;

import javax.servlet.http.HttpServletRequest;

/**
 * Search Page Controller
 * @author klenkiven
 */
@Controller
@RequiredArgsConstructor
public class SearchController {

    private final MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model, HttpServletRequest request) {
        String queryString = request.getQueryString();
        searchParam.set_queryString(queryString);
        SearchResult result = mallSearchService.search(searchParam);
        model.addAttribute("result", result);
        return "list";
    }

}
