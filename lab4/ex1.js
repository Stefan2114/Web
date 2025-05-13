const counties = {
    "Bucharest": ["Sector 1", "Sector 2", "Sector 3", "Sector 4", "Sector 5", "Sector 6"],
    "Cluj": ["Cluj-Napoca", "Turda", "Dej", "Gherla", "Câmpia Turzii"],
    "Timiș": ["Timișoara", "Lugoj", "Jimbolia", "Buziaș", "Făget"],
    "Iași": ["Iași", "Pașcani", "Târgu Frumos", "Hârlău"],
    "Constanța": ["Constanța", "Mangalia", "Medgidia", "Năvodari", "Cernavodă"]
};

const countySelect = document.getElementsByName("county")[0];
const citySelect = document.getElementsByName("city")[0];

for (let county in counties) {
    let option = document.createElement("option");
    option.value = county;
    option.textContent = county;
    countySelect.appendChild(option);
}

countySelect.addEventListener("change", function() {
    citySelect.innerHTML = '<option value="">Select a city</option>';
    let selectedCounty = countySelect.value;
    
    if (selectedCounty && counties[selectedCounty]) {
        counties[selectedCounty].forEach(city => {
            let option = document.createElement("option");
            option.value = city;
            option.textContent = city;
            citySelect.appendChild(option);
        });
    }
});
