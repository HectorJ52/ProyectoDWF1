// ============================================
// SPRING CÁTEDRA - SISTEMA ADMINISTRATIVO
// ============================================

const $ = id => document.getElementById(id);

// ============================================
// MÓDULOS
// ============================================

const modules = {
    clientes: "Clientes",
    cursos: "Cursos",
    diplomados: "Diplomados",
    inscripciones: "Inscripciones",
    pagos: "Pagos",
    docentes: "Docentes",
    categorias: "Categorías",
    modalidades: "Modalidades",
    espacios: "Espacios",
    equipamientos: "Equipamientos",
    "reservas-espacio": "Reservas de espacios",
    "servicios-catering": "Servicios de catering",
    "solicitudes-catering": "Solicitudes de catering",
    cotizaciones: "Cotizaciones",
    "detalles-cotizacion": "Detalles de cotización",
    usuarios: "Usuarios"
};

// ============================================
// PERMISOS POR ROL
// ============================================

const rolePermissions = {

    ADMIN: {
        modules: "*",
        create: "*",
        edit: "*",
        delete: "*"
    },

    RECEPCIONISTA: {
        modules: [
            "clientes", "cursos", "diplomados",
            "inscripciones", "docentes", "categorias",
            "modalidades", "espacios", "equipamientos",
            "reservas-espacio", "servicios-catering",
            "solicitudes-catering", "cotizaciones",
            "detalles-cotizacion"
        ],
        create: [
            "clientes", "inscripciones",
            "reservas-espacio", "solicitudes-catering",
            "cotizaciones"
        ],
        edit: [
            "clientes", "inscripciones",
            "reservas-espacio", "solicitudes-catering",
            "cotizaciones"
        ],
        delete: []
    },

    CONTABILIDAD: {
        modules: [
            "clientes", "cursos", "diplomados",
            "inscripciones", "pagos", "docentes",
            "categorias", "modalidades", "espacios",
            "equipamientos", "reservas-espacio",
            "servicios-catering", "solicitudes-catering",
            "cotizaciones", "detalles-cotizacion"
        ],
        create: ["pagos"],
        edit: ["pagos"],
        delete: ["pagos"]
    },

    USER: {
        modules: [
            "clientes", "cursos", "diplomados",
            "inscripciones", "docentes", "categorias",
            "modalidades", "espacios", "equipamientos",
            "reservas-espacio", "servicios-catering",
            "solicitudes-catering", "cotizaciones",
            "detalles-cotizacion"
        ],
        create: [],
        edit: [],
        delete: []
    },

    CLIENTE: {
        modules: [
            "cursos", "diplomados", "docentes",
            "categorias", "modalidades", "espacios",
            "equipamientos", "reservas-espacio",
            "servicios-catering", "cotizaciones"
        ],
        create: [],
        edit: [],
        delete: []
    }
};

function getRole() {
    return (sessionStorage.getItem("rol") || "")
        .replace(/^ROLE_/, "")
        .toUpperCase();
}

function hasPermission(action, module) {
    const permissions = rolePermissions[getRole()];

    if (!permissions) return false;

    const allowed = permissions[action];

    return allowed === "*" ||
        (Array.isArray(allowed) && allowed.includes(module));
}

function canAccess(module) {
    return hasPermission("modules", module);
}

// ============================================
// DEFINICIÓN DE CAMPOS
// ============================================

const T = (name, label, required = false) =>
    ({name, label, type: "text", required});

const N = (name, label, required = false) =>
    ({name, label, type: "number", required});

const M = (name, label, required = false) =>
    ({name, label, type: "money", required});

const D = (name, label, required = false) =>
    ({name, label, type: "date", required});

const DT = (name, label, required = false) =>
    ({name, label, type: "datetime-local", required});

const H = (name, label, required = false) =>
    ({name, label, type: "time", required});

const E = (name, label, options, required = false) =>
    ({name, label, type: "enum", options, required});

const R = (name, label, module, required = false) =>
    ({name, label, type: "relation", module, required});

const B = (name, label) =>
    ({name, label, type: "checkbox"});

const states = [
    "PENDIENTE",
    "CONFIRMADA",
    "CANCELADA",
    "FINALIZADA"
];

// ============================================
// FORMULARIOS DE LOS 16 MÓDULOS
// ============================================

const formFields = {

    usuarios: [
        T("username", "Nombre de usuario", true),
        {
            name: "correo",
            label: "Correo",
            type: "email",
            required: true
        },
        {
            name: "password",
            label: "Contraseña",
            type: "password",
            required: true
        },
        E("rol", "Rol", [
            "ADMIN",
            "USER",
            "RECEPCIONISTA",
            "CLIENTE",
            "CONTABILIDAD"
        ], true)
    ],

    clientes: [
        T("nombres", "Nombres", true),
        T("apellidos", "Apellidos", true),
        T("documento", "Documento", true),
        {
            name: "correo",
            label: "Correo",
            type: "email",
            required: true
        },
        T("telefono", "Teléfono"),
        T("direccion", "Dirección"),
        B("activo", "Activo")
    ],

    docentes: [
        T("nombres", "Nombres", true),
        T("apellidos", "Apellidos", true),
        T("documento", "Documento", true),
        {
            name: "correo",
            label: "Correo",
            type: "email",
            required: true
        }
    ],

    categorias: [
        T("nombre", "Nombre", true),
        T("descripcion", "Descripción"),
        B("activo", "Activo")
    ],

    modalidades: [
        T("nombre", "Nombre", true),
        T("descripcion", "Descripción")
    ],

    cursos: [
        T("codigo", "Código", true),
        T("nombre", "Nombre", true),
        T("descripcion", "Descripción"),
        R("categoria", "Categoría", "categorias", true),
        R("modalidad", "Modalidad", "modalidades", true),
        R("docente", "Docente", "docentes"),
        N("cupoMaximo", "Cupo máximo", true),
        M("costo", "Costo", true),
        D("fechaInicio", "Fecha de inicio", true),
        D("fechaFin", "Fecha de finalización", true),
        T("horario", "Horario"),
        B("activo", "Activo")
    ],

    diplomados: [
        T("codigo", "Código", true),
        T("nombre", "Nombre", true),
        T("descripcion", "Descripción"),
        R("categoria", "Categoría", "categorias", true),
        R("modalidad", "Modalidad", "modalidades", true),
        R("docente", "Docente", "docentes"),
        N("cupoMaximo", "Cupo máximo", true),
        M("costo", "Costo", true),
        D("fechaInicio", "Fecha de inicio", true),
        D("fechaFin", "Fecha de finalización", true),
        T("horario", "Horario"),
        B("activo", "Activo")
    ],

    inscripciones: [
        R("cliente", "Cliente", "clientes", true),
        R("curso", "Curso", "cursos"),
        R("diplomado", "Diplomado", "diplomados"),
        DT("fechaInscripcion", "Fecha de inscripción", true),
        E("estado", "Estado", states, true),
        B("activo", "Activo")
    ],

    pagos: [
        R("inscripcion", "Inscripción", "inscripciones"),
        R("cotizacion", "Cotización", "cotizaciones"),
        M("monto", "Monto", true),
        DT("fechaPago", "Fecha del pago", true),
        T("metodoPago", "Método de pago", true),
        T("estado", "Estado", true)
    ],

    espacios: [
        T("nombre", "Nombre", true),
        T("descripcion", "Descripción"),
        N("capacidad", "Capacidad", true),
        E("tipoEspacio", "Tipo de espacio", [
            "AUDITORIO",
            "SALA_REUNIONES",
            "LABORATORIO",
            "AULA",
            "SALA_MULTIMEDIA"
        ], true),
        B("activo", "Activo"),
        {
            name: "equipamientos",
            label: "Equipamientos",
            type: "multiple",
            module: "equipamientos"
        }
    ],

    equipamientos: [
        T("nombre", "Nombre", true),
        T("descripcion", "Descripción")
    ],

    "reservas-espacio": [
        R("espacio", "Espacio", "espacios", true),
        R("cliente", "Cliente", "clientes", true),
        D("fechaInicio", "Fecha de inicio", true),
        D("fechaFin", "Fecha de finalización", true),
        H("horaInicio", "Hora de inicio", true),
        H("horaFin", "Hora de finalización", true),
        T("motivo", "Motivo"),
        M("montoTotal", "Monto total", true),
        E("estado", "Estado", states, true)
    ],

    "servicios-catering": [
        T("nombre", "Nombre", true),
        T("descripcion", "Descripción"),
        M("precioPersona", "Precio por persona", true),
        B("activo", "Activo")
    ],

    "solicitudes-catering": [
        R("cliente", "Cliente", "clientes", true),
        R("servicio", "Servicio", "servicios-catering", true),
        N("numeroAsistentes", "Número de asistentes", true),
        T("menu", "Menú"),
        D("fecha", "Fecha", true),
        H("hora", "Hora", true),
        T("lugar", "Lugar"),
        E("estado", "Estado", states, true),
        M("precioTotal", "Precio total", true),
        R("reservaEspacio", "Reserva de espacio", "reservas-espacio"),
        R("cotizacion", "Cotización", "cotizaciones")
    ],

    cotizaciones: [
        R("cliente", "Cliente", "clientes", true),
        DT("fechaCotizacion", "Fecha de cotización", true),
        E("estado", "Estado", [
            "PENDIENTE",
            "EN_PROCESO",
            "APROBADA",
            "RECHAZADA"
        ], true),
        M("total", "Monto total", true)
    ],

    "detalles-cotizacion": [
        R("cotizacion", "Cotización", "cotizaciones", true),
        E("tipoItem", "Tipo de ítem", [
            "CURSO",
            "DIPLOMADO",
            "ESPACIO",
            "CATERING"
        ], true),
        N("idReferencia", "ID de referencia", true),
        N("cantidad", "Cantidad", true),
        M("precioUnitario", "Precio unitario", true),
        M("subtotal", "Subtotal", true)
    ]
};

// ============================================
// VARIABLES GENERALES
// ============================================

let token = sessionStorage.getItem("token");
let currentModule = null;
let currentPage = 0;
let totalPages = 1;
let currentRequest = 0;

// ============================================
// CONEXIÓN CON SPRING BOOT
// ============================================

async function api(path, options = {}) {

    const headers = {
        ...(options.body
            ? {"Content-Type": "application/json"}
            : {}),
        ...(token
            ? {"Authorization": "Bearer " + token}
            : {}),
        ...options.headers
    };

    const response = await fetch(path, {
        ...options,
        headers
    });

    if (!response.ok) {

        let detail = "";

        try {
            const data = await response.json();

            detail = data.mensaje ||
                     data.message ||
                     data.error ||
                     "";
        } catch (_) {}

        if (response.status === 401) {
            throw new Error("La sesión ha expirado.");
        }

        if (response.status === 403) {
            throw new Error("No tienes permiso para esta operación.");
        }

        throw new Error(
            "Error " + response.status +
            (detail ? ": " + detail : "")
        );
    }

    if (response.status === 204) {
        return null;
    }

    const body = await response.text();

    return body ? JSON.parse(body) : null;
}

// ============================================
// MENSAJES
// ============================================

function notify(message, error = false) {

    const notice = $("notice");

    notice.textContent = message;
    notice.className = error ? "notice error" : "notice";
    notice.hidden = false;
}

// ============================================
// LOGIN
// ============================================

$("loginForm").addEventListener("submit", async event => {

    event.preventDefault();

    const message = $("loginMessage");
    message.textContent = "Verificando credenciales...";

    try {

        const data = await api("/api/auth/login", {
            method: "POST",
            body: JSON.stringify({
                usuario: $("usuario").value.trim(),
                password: $("password").value
            })
        });

        if (!data.token) {
            throw new Error("No se recibió el token.");
        }

        token = data.token;

        sessionStorage.setItem("token", data.token);
        sessionStorage.setItem("usuario", data.username);
        sessionStorage.setItem("rol", data.rol);

        $("password").value = "";
        message.textContent = "";

        showApp();

    } catch (error) {
        message.textContent = error.message;
    }
});

// ============================================
// PANEL PRINCIPAL
// ============================================

function showApp() {

    $("loginView").hidden = true;
    $("appView").hidden = false;

    $("currentUser").textContent =
        sessionStorage.getItem("usuario") || "Usuario";

    $("currentRole").textContent = getRole();

    createMenu();
    showDashboard();
}

// ============================================
// MENÚ SEGÚN EL ROL
// ============================================

function createMenu() {

    const menu = $("menu");
    menu.replaceChildren();

    const home = document.createElement("button");

    home.textContent = "Panel principal";
    home.onclick = showDashboard;

    menu.appendChild(home);

    let count = 0;

    Object.entries(modules).forEach(([key, title]) => {

        if (!canAccess(key)) return;

        const button = document.createElement("button");

        button.textContent = title;
        button.dataset.module = key;
        button.onclick = () => openModule(key);

        menu.appendChild(button);
        count++;
    });

    $("moduleCount").textContent = count;
}

// ============================================
// DASHBOARD
// ============================================

function showDashboard() {

    currentModule = null;
    currentRequest++;

    $("pageTitle").textContent = "Panel principal";
    $("pageSubtitle").textContent =
        "Sistema de gestión académica";

    $("dashboard").hidden = false;
    $("moduleView").hidden = true;
    $("notice").hidden = true;

    document.querySelectorAll("#menu button").forEach(button => {
        button.classList.remove("active");
    });
}

// ============================================
// ABRIR MÓDULO
// ============================================

function openModule(module) {

    if (!canAccess(module)) return;

    currentModule = module;
    currentPage = 0;

    $("dashboard").hidden = true;
    $("moduleView").hidden = false;
    $("editor").hidden = true;
    $("notice").hidden = true;

    $("pageTitle").textContent = modules[module];
    $("pageSubtitle").textContent =
        "Administración de " + modules[module];

    // Ocultar Nuevo si el rol no puede crear.
    $("newBtn").hidden =
        !hasPermission("create", module);

    document.querySelectorAll("#menu button").forEach(button => {

        button.classList.toggle(
            "active",
            button.dataset.module === module
        );
    });

    loadRecords();
}

// ============================================
// CONSULTAR REGISTROS
// ============================================

function getItems(data) {

    if (Array.isArray(data)) return data;
    if (Array.isArray(data?.content)) return data.content;
    if (Array.isArray(data?.data)) return data.data;

    return [];
}

async function loadRecords() {

    if (!currentModule) return;

    const module = currentModule;
    const requestId = ++currentRequest;

    $("recordInfo").textContent = "Cargando registros...";
    $("recordsTable").replaceChildren();

    try {

        const data = await api(
            "/api/" + module +
            "?page=" + currentPage +
            "&size=10"
        );

        if (requestId !== currentRequest) return;

        const records = getItems(data);

        totalPages = Array.isArray(data)
            ? 1
            : (data?.totalPages ??
               data?.page?.totalPages ??
               1);

        drawTable(records);

        $("recordInfo").textContent =
            records.length + " registros mostrados";

        $("pageInfo").textContent =
            "Página " + (currentPage + 1) +
            " de " + Math.max(1, totalPages);

        $("prevBtn").disabled = currentPage === 0;
        $("nextBtn").disabled = currentPage + 1 >= totalPages;

    } catch (error) {

        if (requestId !== currentRequest) return;

        $("recordInfo").textContent =
            "No se pudieron cargar los registros.";

        notify(error.message, true);
    }
}

// ============================================
// IDENTIFICAR ID
// ============================================

function findId(record) {

    if (record.id != null) return record.id;

    const key = Object.keys(record).find(k =>
        /^id[A-Z_]/.test(k) || /^id_/.test(k)
    );

    return key ? record[key] : undefined;
}

// ============================================
// TABLAS CON BOTONES SEGÚN EL ROL
// ============================================

function drawTable(records) {

    const table = $("recordsTable");
    table.replaceChildren();

    if (!records.length) {

        table.insertRow().insertCell().textContent =
            "No hay registros para mostrar.";

        return;
    }

    const columns = [...new Set(
        records.flatMap(record => Object.keys(record))
    )].slice(0, 7);

    const header = table.createTHead().insertRow();

    columns.forEach(key => {

        const th = document.createElement("th");

        th.textContent = key;
        header.appendChild(th);
    });

    const canEdit = hasPermission("edit", currentModule);
    const canDelete = hasPermission("delete", currentModule);

    if (canEdit || canDelete) {

        const th = document.createElement("th");
        th.textContent = "Acciones";
        header.appendChild(th);
    }

    const tbody = table.createTBody();

    records.forEach(record => {

        const row = tbody.insertRow();

        columns.forEach(key => {

            const value = record[key];

            row.insertCell().textContent =
                value == null ? "—" :
                typeof value === "object"
                    ? JSON.stringify(value).slice(0, 100)
                    : String(value);
        });

        const id = findId(record);

        if (canEdit || canDelete) {

            const cell = row.insertCell();

            if (canEdit) {

                const edit = document.createElement("button");

                edit.textContent = "Editar";
                edit.className = "secondary";
                edit.disabled = id == null;
                edit.onclick = () => editRecord(id);

                cell.appendChild(edit);
            }

            if (canDelete) {

                const remove = document.createElement("button");

                remove.textContent = "Eliminar";
                remove.className = "secondary";
                remove.disabled = id == null;
                remove.onclick = () => deleteRecord(id);

                cell.appendChild(remove);
            }
        }
    });
}

// ============================================
// RELACIONES ENTRE MÓDULOS
// ============================================

function getRecordName(record) {

    if (record.nombre) return record.nombre;

    if (record.nombres || record.apellidos) {

        return [
            record.nombres,
            record.apellidos
        ].filter(Boolean).join(" ");
    }

    if (record.username) return record.username;
    if (record.codigo) return record.codigo;

    const id = findId(record);

    return "Registro #" + (id ?? "");
}

async function loadRelationOptions(field, select, selected) {

    select.replaceChildren();

    const empty = document.createElement("option");

    empty.value = "";
    empty.textContent = field.required
        ? "Selecciona una opción"
        : "Ninguno";

    select.appendChild(empty);

    try {

        const data = await api(
            "/api/" + field.module + "?page=0&size=1000"
        );

        const records = getItems(data);

        records.forEach(record => {

            const id = findId(record);

            if (id == null) return;

            const option = document.createElement("option");

            option.value = id;

            option.textContent =
                getRecordName(record) + " (#" + id + ")";

            if (field.type === "multiple") {

                option.selected =
                    Array.isArray(selected) &&
                    selected.some(item =>
                        String(item?.id ?? item) === String(id)
                    );

            } else {

                option.selected =
                    String(id) === String(selected);
            }

            select.appendChild(option);
        });

    } catch (error) {

        const option = document.createElement("option");

        option.value = "";
        option.textContent =
            "No se pudieron cargar las opciones";

        select.appendChild(option);

        notify(
            "No se pudo cargar " + field.label +
            ": " + error.message,
            true
        );
    }
}

// ============================================
// CONSTRUIR FORMULARIOS
// ============================================

async function buildForm(module, data = {}) {

    const container = $("recordFields");

    container.replaceChildren();

    const fields = formFields[module] || [];

    $("jsonEditor").hidden = true;
    container.hidden = false;

    const pending = [];

    fields.forEach(field => {

        const group = document.createElement("div");
        group.className = "field-group";

        const label = document.createElement("label");

        label.htmlFor = "field_" + field.name;

        label.textContent =
            field.label + (field.required ? " *" : "");

        let input;

        if (
            field.type === "enum" ||
            field.type === "relation" ||
            field.type === "multiple"
        ) {

            input = document.createElement("select");

            if (field.type === "multiple") {
                input.multiple = true;
                input.size = 5;
            }

            if (field.type === "enum") {

                const placeholder = document.createElement("option");

                placeholder.value = "";
                placeholder.textContent = "Selecciona una opción";

                input.appendChild(placeholder);

                field.options.forEach(value => {

                    const option = document.createElement("option");

                    option.value = value;
                    option.textContent = value;

                    input.appendChild(option);
                });
            }

        } else {

            input = document.createElement("input");

            input.type = field.type === "money"
                ? "number"
                : field.type;

            if (field.type === "money") {
                input.step = "0.01";
                input.min = "0";
            }

            if (field.type === "number") {
                input.step = "1";
            }
        }

        input.id = "field_" + field.name;
        input.name = field.name;
        input.required = Boolean(field.required);

        if (field.type === "checkbox") {
            input.checked = data[field.name] ?? true;
        }

        const value = data[field.name];

        if (
            field.type === "relation" ||
            field.type === "multiple"
        ) {

            const selected = field.type === "multiple"
                ? value
                : (value?.id ??
                   data[
                       "id" +
                       field.name[0].toUpperCase() +
                       field.name.slice(1)
                   ] ??
                   "");

            pending.push(
                loadRelationOptions(field, input, selected)
            );

        } else if (
            field.type !== "password" &&
            field.type !== "checkbox" &&
            value != null
        ) {

            input.value = String(value).slice(
                0,
                field.type === "datetime-local"
                    ? 16
                    : undefined
            );
        }

        if (field.type === "password") {
            input.autocomplete = "new-password";
        }

        group.append(label, input);
        container.appendChild(group);
    });

    await Promise.all(pending);
}

// ============================================
// OBTENER DATOS
// ============================================

function getFormData() {

    const fields = formFields[currentModule] || [];
    const data = {};

    fields.forEach(field => {

        const input = $("field_" + field.name);

        if (field.type === "checkbox") {

            data[field.name] = input.checked;
            return;
        }

        if (field.type === "multiple") {

            data[field.name] = Array.from(
                input.selectedOptions
            ).map(option => ({
                id: Number(option.value)
            }));

            return;
        }

        const value = input.value.trim();

        if (!value && !field.required) {
            return;
        }

        if (field.type === "relation") {

            data[field.name] = value
                ? {id: Number(value)}
                : null;

            return;
        }

        if (
            field.type === "number" ||
            field.type === "money"
        ) {

            data[field.name] = value === ""
                ? null
                : Number(value);

            return;
        }

        data[field.name] = value;
    });

    if (currentModule === "usuarios") {

        data.rol = {
            nombre: data.rol
        };

        data.activo = true;
    }

    return data;
}

// ============================================
// NUEVO REGISTRO
// ============================================

async function newRecord() {

    if (!hasPermission("create", currentModule)) return;

    const module = currentModule;

    $("editor").hidden = false;
    $("editorTitle").textContent = "Nuevo registro";
    $("recordId").value = "";

    await buildForm(module);

    if (module !== currentModule) return;

    $("editor").scrollIntoView({
        behavior: "smooth"
    });
}

// ============================================
// EDITAR
// ============================================

async function editRecord(id) {

    if (!hasPermission("edit", currentModule)) return;

    try {

        const module = currentModule;

        const data = await api(
            "/api/" + module +
            "/" + encodeURIComponent(id)
        );

        if (module !== currentModule) return;

        $("editor").hidden = false;
        $("editorTitle").textContent = "Editar registro " + id;
        $("recordId").value = id;

        await buildForm(module, data);

        if (module !== currentModule) return;

        $("editor").scrollIntoView({
            behavior: "smooth"
        });

    } catch (error) {

        notify(error.message, true);
    }
}

// ============================================
// GUARDAR
// ============================================

$("recordForm").addEventListener("submit", async event => {

    event.preventDefault();

    if (!currentModule) return;

    const id = $("recordId").value;
    const operation = id ? "edit" : "create";

    if (!hasPermission(operation, currentModule)) {
        notify("Operación no disponible.", true);
        return;
    }

    try {

        const data = getFormData();

        const path = "/api/" + currentModule +
            (id ? "/" + encodeURIComponent(id) : "");

        await api(path, {
            method: id ? "PUT" : "POST",
            body: JSON.stringify(data)
        });

        $("editor").hidden = true;

        await loadRecords();

        notify("Registro guardado correctamente.");

    } catch (error) {

        notify(
            "No se pudo guardar: " + error.message,
            true
        );
    }
});

// ============================================
// ELIMINAR
// ============================================

async function deleteRecord(id) {

    if (!hasPermission("delete", currentModule)) return;

    if (!confirm(
        "¿Seguro que deseas eliminar o inactivar este registro?"
    )) return;

    try {

        await api(
            "/api/" + currentModule +
            "/" + encodeURIComponent(id),
            {method: "DELETE"}
        );

        await loadRecords();

        notify("Operación realizada correctamente.");

    } catch (error) {

        notify(error.message, true);
    }
}

// ============================================
// BOTONES
// ============================================

$("newBtn").onclick = newRecord;
$("refreshBtn").onclick = loadRecords;

$("cancelBtn").onclick = () => {
    $("editor").hidden = true;
};

$("prevBtn").onclick = () => {

    if (currentPage > 0) {
        currentPage--;
        loadRecords();
    }
};

$("nextBtn").onclick = () => {

    if (currentPage + 1 < totalPages) {
        currentPage++;
        loadRecords();
    }
};

// ============================================
// CERRAR SESIÓN
// ============================================

$("logoutBtn").onclick = () => {

    sessionStorage.removeItem("token");
    sessionStorage.removeItem("usuario");
    sessionStorage.removeItem("rol");

    token = null;
    currentModule = null;
    currentRequest++;

    $("appView").hidden = true;
    $("loginView").hidden = false;

    $("loginMessage").textContent = "";
};

// ============================================
// RESTAURAR SESIÓN
// ============================================

if (token) {
    showApp();
}