let add_movie_form = $("#add-star-db-form")

function handleSuccess(resultData) {

    console.log(resultData)

    let resultLog = $("#add-result")

    if (resultData["status"] === "success") {
        let resultP = `<p class="result-message">Success: starId:${resultData["data"]["starId"]}; </p>`;
        resultLog.append(resultP)
    } else {
        let resultP = `<p class="result-message fail">Error: ${resultData["message"]}</p>`
        resultLog.append(resultP)
    }
}

function handleError(resultError) {
    let jsonError = JSON.parse(resultError)
    Toastify({
        text: jsonError["message"],
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
        url: "api/add-star",
        data: add_movie_form.serialize(),
        success: (resultData) => handleSuccess(resultData),
        error: (jqXHR) => handleError(jqXHR.responseText)
    })
}

add_movie_form.submit(handleSubmitAddMovie)