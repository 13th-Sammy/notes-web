const API_BASE=window.location.origin;

export async function post(path, data) {
    const response=await fetch(`${API_BASE}${path}`, {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify(data),
    });
    return await response.json();
}