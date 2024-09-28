let autocompleteInputComp = $("#autocomplete")
let suggestionResultsComp = $("#suggestion-results")
let searchAutoCompleteForm = $("#search-autocomplete-form")

function formatSuggestionKey(keywords) {
    return `sug_${keywords.toLowerCase().trim().replace(' ', '%')}`
}

// export function handleAutocompleteInput(event) {
//     event.preventDefault()
//
//     if (autocompleteInputComp.val().length >= 3) {
//         setTimeout(() => {
//             fetchOrSaveSearchAutocomplete(autocompleteInputComp.val())
//         }, 300)
//     } else {
//         suggestionResultsComp.empty()
//         suggestionResultsComp.css("visibility", "hidden")
//     }
// }

export function handleAutocompleteInputChange(event) {
    event.preventDefault()
    if (autocompleteInputComp && autocompleteInputComp.val().length < 3) {
        suggestionResultsComp.empty()
        suggestionResultsComp.css("visibility", "hidden")
    }
}

function fetchOrSaveSearchAutocomplete(keywords) {
    if (keywords.length < 3) return

    let suggestionResultCache = localStorage.getItem(formatSuggestionKey(keywords));

    if (suggestionResultCache) {
        handleGetCachedSuggestionResult(suggestionResultCache, keywords)
    } else {
        console.log("## Fetch suggestions from api")
        let timeoutId;
        $.ajax({
            dataType: "json",
            method: "GET",
            url: `api/movie-list/search/suggestion?keywords=${keywords}`,
            success: (result) => handleFetchedSuggestionResult(result, keywords)
        })
    }
}

/**
 *
 * @param data array of suggestion movie object
 */
function renderSearchAutoCompleteResult(data) {
    if (data.length > 0) suggestionResultsComp.css('visibility', 'visible')
    else suggestionResultsComp.css('visibility', 'hidden')

    // Empty suggestion results component
    suggestionResultsComp.empty()
    data.forEach(movie => {
        let movieHTML = `
        <a class="search-dropdown-item" href="./single-movie.jsp?id=${movie["id"]}" data-index="${movie["id"]}">
            <div class="search-dropdown-item-detail">
                <h4>${movie["title"]}</h4>
                <p><span>${movie["genreName"]}</span><span>${movie["year"]}</span></p>
            </div>
        </a>
        `

        suggestionResultsComp.append(movieHTML)
    })
}

function handleGetCachedSuggestionResult(cacheResult, keywords) {
    // Parse cache data
    let cacheData = JSON.parse(cacheResult)

    // Show results requested from front-end cache
    console.log("## Get suggestions from cache")
    console.log(`Keywords: '${keywords}'`)
    console.log("Results: ", cacheData)

    console.log("cache Data: ", typeof cacheData)

    // Render data
    renderSearchAutoCompleteResult(cacheData)
}

function handleFetchedSuggestionResult(results, keywords) {
    let data = results["data"]

    // Show results requested from backend
    console.log("Get suggestion from backend")
    console.log(`Keywords: '${keywords}'`)
    console.log("Results: ", data)

    // Render data
    renderSearchAutoCompleteResult(data)

    // Cache the suggestion result
    localStorage.setItem(formatSuggestionKey(keywords), JSON.stringify(data))
}

let selectedIndex = -1
let currentSuggestionHref
function selectAutocompleteResult(event) {

    if (suggestionResultsComp.css("visibility") === "visible") {
        const items = suggestionResultsComp.find('.search-dropdown-item');

        switch(event.which) {
            case 38:
                if (selectedIndex > 0) {
                    selectedIndex--;
                } else {
                    selectedIndex = items.length - 1;
                }
                break;
            case 40:
                if (selectedIndex < items.length - 1) {
                    selectedIndex++;
                } else {
                    selectedIndex = 0;
                }
                break;
        }
        items.removeClass('active');
        items.eq(selectedIndex).addClass('active');
        currentSuggestionHref = items.eq(selectedIndex).attr("href")

        let activeItem = items.eq(selectedIndex)
        let activeItemTop = activeItem.offset().top
        let activeItemHeight = activeItem.height()
        let containerTop = suggestionResultsComp.offset().top
        let containerHeight = suggestionResultsComp.height()
        const scrollOffset = activeItemTop - containerTop;
        if (scrollOffset < 0 || scrollOffset + activeItemHeight > containerHeight) {
            suggestionResultsComp.scrollTop(suggestionResultsComp.scrollTop() + scrollOffset);
        }
    }
}

function handleSearchInputKeyup(event) {
    // Handle enter
    if (event.key === "ArrowUp" || event.key === "ArrowDown") {
        event.preventDefault();
        selectAutocompleteResult(event)
    }

    if (event.key === "Esc") {
        console.log("escape hit")
        suggestionResultsComp.empty()
        suggestionResultsComp.css("visibility", "hidden")
    }
}

function handleSubmitSearchFulltext(event) {
    event.preventDefault()

    if (!currentSuggestionHref) {
        // Pass keywords to movie-list page and use it to retrieve the results
        let keywords = autocompleteInputComp.val()
        window.location.href = `./movie-list.jsp?search=true&keywords=${keywords}`
    } else {
        // Proceed to single page
        if (suggestionResultsComp.css("visibility") === "visible") {
            window.location.href = currentSuggestionHref
        }
    }
}

// autocompleteInputComp.on("input", handleAutocompleteInput)
autocompleteInputComp.autocomplete({
    lookup: (keywords) => fetchOrSaveSearchAutocomplete(keywords),
    deferRequestBy: 300
})
autocompleteInputComp.on("input", handleAutocompleteInputChange)
autocompleteInputComp.on("keyup",handleSearchInputKeyup)
searchAutoCompleteForm.submit(handleSubmitSearchFulltext)