import { getParameterByName } from "./utils.js";

function getCurrentPathQuery() {
    let currentUrl = window.location.href
    let fabflixIndex = currentUrl.indexOf("fabflix")
    let pageIndex = currentUrl.indexOf("&page")
    return pageIndex === -1 ?
        currentUrl.substring(fabflixIndex + 7) : currentUrl.substring(fabflixIndex + 7, pageIndex);
}

export function getCurrentPage() {
    let pageParam = getParameterByName("page")
    return pageParam === null ? 1 : parseInt(pageParam);
}

export function getCurrentPageSize() {
    let pageSizeParam = getParameterByName("pageSize")
    return pageSizeParam === null ? 10 : parseInt(pageSizeParam);
}

function getLastPage(totalItems) {
    return Math.ceil(totalItems / getCurrentPageSize())
}

function setCurrentPageSize() {
    let pageCountSelect = document.getElementById("page-count-selection")
    let pageSize = getCurrentPageSize()
    if (pageSize !== 10) {
        pageCountSelect.value = pageSize
    }
}

function setCurrentActivePageUrl(page) {
    let currentPageLink = document.getElementById(`page-btn-${1}`)

    if (page !== null) currentPageLink = document.getElementById(`page-btn-${page}`)

    currentPageLink.classList.add("active")
    currentPageLink.addEventListener("click", (event) => {
        event.preventDefault()
    })
}

export function renderPaginationInfo(totalItems, page) {
    let pageLinkSection = document.getElementById("page-links-section")
    let nextPageBtn = document.getElementById("next-page")

    // Set current pageSize of selection component from pageSize param in url
    setCurrentPageSize()

    let pageSize = getCurrentPageSize()
    let lastPage = getLastPage(totalItems)
    let currentPage = getCurrentPage()

    if (pageSize < totalItems) {
        // extract current page path and query
        let currentPath = getCurrentPathQuery()

        // Default start page in between first and last page
        let startPage = 1;
        if (currentPage <= 5) {
            startPage = 2;
        } else if (currentPage >= lastPage - 5) {
            startPage = Math.max(2, lastPage - 9);
        } else {
            startPage = currentPage - 5;
        }

        for (let i = 1; i <= Math.min(lastPage, 11); i++) {
            let pageUrl = document.createElement("a")
            pageUrl.className = "page-btn"
            if (i === 1) {
                pageUrl.id = `page-btn-1`
                pageUrl.href= `.${currentPath}&page=${1}&pageSize=${pageSize}`
                pageUrl.innerText = "First"
            } else if (i === Math.min(lastPage, 11) ) {
                pageUrl.id = `page-btn-${lastPage}`
                pageUrl.href= `.${currentPath}&page=${lastPage}&pageSize=${pageSize}`
                pageUrl.innerText = "Last"
            } else {
                let pageNumber = startPage + i - 2;
                pageUrl.id = `page-btn-${pageNumber}`
                pageUrl.href= `.${currentPath}&page=${pageNumber}&pageSize=${pageSize}`
                pageUrl.innerText = `${pageNumber}`
            }
            pageLinkSection.insertBefore(pageUrl, nextPageBtn)
        }

        // Highlight current page in the pagination
        setCurrentActivePageUrl(page)

        // Render total record in pagination
        let totalRecord = document.getElementById("total-records")
        totalRecord.innerText = `${totalItems}`
    }


}

export function toNextPage() {
    let currentPage = getCurrentPage()
    let totalRecordComponent = document.getElementById("total-records")
    let totalRecords = parseInt(
        totalRecordComponent.innerText.match(/\d+/)?.[0] || 0, 10
    );
    let pageCountSelect = document.getElementById("page-count-selection")
    let lastPage = Math.ceil(totalRecords / pageCountSelect.value)

    if (currentPage < lastPage) {
        let url = new URL(window.location.href);
        let params = new URLSearchParams(url.search);
        params.set("page", currentPage + 1);
        window.location.href = url.origin + url.pathname + '?' + params.toString();
    }
}

export function toPrevPage() {
    let currentPage = getCurrentPage()
    let currentPageSize = getCurrentPageSize()

    if (currentPage > 1) {
        let url = new URL(window.location.href);
        let params = new URLSearchParams(url.search);
        params.set("page", currentPage - 1);
        params.set("pageSize", currentPageSize)
        window.location.href = url.origin + url.pathname + '?' + params.toString();
    }
}

export function disableNextPrevButtons(totalItems) {
    let currentPage = getCurrentPage()
    let lastPage = getLastPage(totalItems)
    const prevButton = document.getElementById("prev-page")
    const nextButton = document.getElementById("next-page")

    prevButton.disabled = currentPage === 1;
    nextButton.disabled = currentPage === lastPage;
}
