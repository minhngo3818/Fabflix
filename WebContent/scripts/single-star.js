import { getParameterByName } from "./utils.js";

function renderData(starData) {

    // Render star information
    let starInfo = jQuery("#star-info")
    let starInfoHTML = `
        <h3>${starData[0]["name"]}</h3>
        <p>Birth Year: ${starData[0]["birthYear"] ?? "N/A"}</p>
    `
    starInfo.append(starInfoHTML)

    // Render movie list
    let starMoviesTbody = jQuery("#star-movies-tbody")
    let movieRowHTML = starData[0]["movies"].map(movie =>
        `
            <tr>
                <td><a href="./single-movie.jsp?id=${movie["id"]}">${movie["title"]}</a></td>
                <td>${movie["year"]}</td>
                <td>${movie["director"]}</td>
            </tr>
        `
        ).join("")
        starMoviesTbody.append(movieRowHTML)
}

let starId = getParameterByName("id")

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: `api/single-star?id=${starId}`,
    success: (result) => renderData(result)
})
