let add_movie_form = $("#add-movie-db-form")

function handleSuccess(resultData) {

    console.log(resultData)

    let resultLog = $("#add-result")

    if (resultData["status"] === "success") {
        let resultP = `<p class="result-message">Success: movieId:${resultData["data"]["movieId"]};
                    starId:${resultData["data"]["starId"]}; genreId:${resultData["data"]["genreId"]}</p>`;
        resultLog.append(resultP)
    } else {
        let resultP = `<p class="result-message fail">Error: ${resultData["message"]}</p>`
        resultLog.append(resultP)
    }
}

function handleError(resultError) {
    let jsonResult = JSON.parse(resultError)
    Toastify({
        text: jsonResult["message"],
        gravity: "top",
        position: "center",
        duration: 2000,
        close: true,
        backgroundColor: "#ca3f49",
    }).showToast();
}


function handleSubmitAddMovie(formSubmitEvent) {
    formSubmitEvent.preventDefault()
    jQuery.ajax({
        dataType: "json",
        method: "POST",
        url: "api/add-movie",
        data: add_movie_form.serialize(),
        success: (resultData) => handleSuccess(resultData),
        error: (jqXHR) => handleError(jqXHR.responseText)
    })
}

add_movie_form.submit(handleSubmitAddMovie)