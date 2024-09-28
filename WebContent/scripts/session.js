function handleSessionResult(result) {
    let sessionDataComp = $("#session-data")
    let sessionDataHTML = `
            <h4>${result["greetings"]}</h4>
            <p><span>Username: </span><span>${result["username"]}</span></p>
            <p><span>Access count: </span><span>${result["accessCount"]}</span></p>
            <p><span>Creation time: </span><span>${result["creationTime"]}</span></p>
            <p><span>Last accessed time: </span><span>${result["lastAccessedTime"]}</span></p>
    `
    sessionDataComp.append(sessionDataHTML)
}



jQuery.ajax({
    method: "GET",
    url: "api/session",
    success: (result) => handleSessionResult(result)
})