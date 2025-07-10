const API_BASE="http://localhost:8080";

export async function post(path, data) {
    const response=await fetch(`${API_BASE}${path}`, {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify(data),
    });
    return await response.json();
}