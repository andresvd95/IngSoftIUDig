"""Genera el informe EA2 (PDF) a partir de docs/ea2/img. Uso: python build_ea2.py  (requiere Chrome + playwright)."""
import base64
import pathlib
from playwright.sync_api import sync_playwright

HERE = pathlib.Path(__file__).parent
IMG = HERE / "img"
OUT = HERE.parent / "Henao_Juan_David_Uribe_Jorge_Eliecer_Valencia_Santiago_Andres_EA2.pdf"
REPO = "https://github.com/andresvd95/IngSoftIUDig.git"
VIDEO_URL = "PENDIENTE: pegar aquí la URL del video de YouTube"  # el video lo sube el equipo


def img(name, width="100%"):
    data = base64.b64encode((IMG / name).read_bytes()).decode()
    return f"<img src='data:image/png;base64,{data}' style='width:{width}'>"


def fig(name, caption, width="100%"):
    return f"<figure>{img(name, width)}<figcaption>{caption}</figcaption></figure>"


def table(head, rows):
    h = "".join(f"<th>{c}</th>" for c in head)
    r = "".join("<tr>" + "".join(f"<td>{c}</td>" for c in row) + "</tr>" for row in rows)
    return f"<table><tr>{h}</tr>{r}</table>"


RF = [
    ("RF-04", "Registrar cliente", "El sistema debe permitir registrar un cliente con tipo y número de identificación, nombre, correo y teléfono."),
    ("RF-05", "Consultar y buscar clientes", "El sistema debe listar los clientes y permitir buscarlos por nombre o identificación."),
    ("RF-06", "Editar y eliminar cliente", "El sistema debe permitir actualizar los datos de un cliente y eliminarlo cuando ninguno de sus vehículos esté dentro del parqueadero."),
    ("RF-07", "Registrar vehículo", "El sistema debe asociar vehículos (placa, tipo, marca, modelo, color) a un cliente existente."),
    ("RF-08", "Crear celda", "El sistema debe permitir crear celdas con número único y tipo de vehículo admitido."),
    ("RF-09", "Editar y eliminar celda", "El sistema debe permitir modificar o eliminar una celda siempre que no esté ocupada."),
    ("RF-10", "Consultar estado de celdas", "El sistema debe mostrar cada celda con su tipo y estado (disponible u ocupada)."),
]
RNF = [
    ("RNF-04", "Integridad", "No pueden existir dos clientes con el mismo tipo y número de identificación, dos vehículos con la misma placa ni dos celdas con el mismo número (validación en servicio e índices únicos)."),
    ("RNF-05", "Concurrencia", "Las operaciones sobre celdas y estancias usan bloqueo pesimista (SELECT … FOR UPDATE) para evitar condiciones de carrera."),
    ("RNF-06", "Usabilidad", "Los errores de validación y de reglas de negocio se muestran en la interfaz con un mensaje claro (HTTP 400, 404 y 409)."),
    ("RNF-07", "Rendimiento", "Los listados responden de forma paginada y en menos de 2 segundos con hasta 1.000 registros."),
    ("RNF-08", "Mantenibilidad", "Arquitectura por capas (controller, service, persistence), migraciones Flyway y pruebas automatizadas."),
]
HU = [
    ("HU-04", "Registrar cliente", "operador", "registrar los datos de un cliente", "tener su información disponible al ingresar sus vehículos",
     ["Tipo, número de identificación y nombre son obligatorios; el correo debe tener formato válido.",
      "Si ya existe un cliente con el mismo tipo y número se rechaza (HTTP 409).",
      "Al guardar, el cliente aparece de inmediato en el listado y persiste al recargar."]),
    ("HU-05", "Consultar, editar y eliminar clientes", "operador", "buscar, modificar y eliminar clientes", "mantener actualizada la base de usuarios",
     ["El listado se filtra por nombre o identificación.",
      "La edición valida los mismos campos y rechaza duplicados (409).",
      "No se puede eliminar un cliente con un vehículo dentro del parqueadero (409); si no existe, se informa (404)."]),
    ("HU-06", "Registrar vehículo del cliente", "operador", "asociar un vehículo a un cliente", "poder registrar su ingreso por placa",
     ["Placa, tipo de vehículo y cliente son obligatorios.",
      "Una placa ya registrada se rechaza (409); el cliente o tipo inexistente devuelve 404.",
      "El vehículo queda disponible en la vista de ingreso."]),
    ("HU-07", "Crear celda", "operador", "crear una celda indicando número y tipo", "ampliar la capacidad del parqueadero",
     ["Número y tipo de vehículo son obligatorios.",
      "Un número de celda duplicado se rechaza (409).",
      "La nueva celda queda Disponible y visible en el estado de celdas."]),
    ("HU-08", "Editar y eliminar celda", "operador", "modificar o eliminar una celda", "reflejar cambios en la distribución física",
     ["Una celda ocupada no se puede editar ni eliminar (409).",
      "El nuevo número no puede coincidir con el de otra celda (409).",
      "Al eliminar, la celda desaparece del listado."]),
    ("HU-09", "Consultar estado de celdas", "operador", "ver todas las celdas con su estado", "saber dónde puedo ubicar un vehículo",
     ["Cada celda muestra número, tipo y estado (Disponible/Ocupada).",
      "El estado se lee del backend y se actualiza tras cada ingreso o salida."]),
]


def pruebas_rows():
    ns = {}
    exec((HERE / "pruebas.py").read_text(encoding="utf-8"), ns)
    return ns["PRUEBAS"]


def build_html():
    s = []
    s.append(f"""<section class='cover'><div class='sp'></div>
<h1>Evidencia de Aprendizaje 2. Diseño Sistema de Información – Iteración 2</h1>
<p class='big'>Gestión de usuarios y gestión de celdas</p><div class='sp'></div>
<p>Juan David Henao Cárdenas<br>Jorge Eliécer Uribe Pulgarín<br>Santiago Andrés Valencia Duque</p><div class='sp'></div>
<p>Institución Universitaria Digital de Antioquia IU Digital<br>Ingeniería de Software y Datos<br>Ingeniería de Software<br>Profesor: Reinaldo Rojas López<br>2026</p>
<p>Grupo 18</p><p class='small'>Repositorio GitHub: {REPO}</p></section>""")
    s.append("""<section><h2>Introducción</h2>
<p>Este documento presenta los entregables de la Iteración 2 del sistema de información del parqueadero «Autos Colombia»: la gestión de usuarios (clientes y sus vehículos) y la gestión de celdas. Sobre la base de la Iteración 1 (entrada y salida de vehículos) se especifican los requerimientos funcionales y no funcionales, los casos de uso, las historias de usuario, los mockups, los diagramas de secuencia de ambas iteraciones, el diagrama de componentes y el plan de pruebas resumido.</p>
<p>La implementación correspondiente está en el repositorio de GitHub: backend en Spring Boot (Java 21, MySQL 8 y Flyway) y frontend en Angular, ambos integrados mediante una API REST.</p>
<h2>Contenido de los entregables</h2><ol>
<li>Requerimientos funcionales y no funcionales</li><li>Diagrama de casos de uso</li><li>Historias de usuario</li><li>Mockups</li>
<li>Diagramas de secuencia (iteraciones 1 y 2)</li><li>Diagrama de componentes</li><li>Plan de pruebas resumido</li><li>Repositorio y video</li></ol></section>""")
    s.append("<section><h2>1. Requerimientos funcionales y no funcionales</h2><h3>Requerimientos funcionales</h3>"
             + table(["ID", "Nombre", "Descripción"], RF)
             + "<h3>Requerimientos no funcionales</h3>" + table(["ID", "Atributo", "Descripción"], RNF)
             + "<p class='small'>Los requerimientos RF-01 a RF-03 y RNF-01 a RNF-03 corresponden a la Iteración 1 (entrada y salida de vehículos).</p></section>")
    s.append("<section><h2>2. Diagrama de casos de uso</h2>"
             + fig("casos-uso-iteracion2.png", "Figura 1. Casos de uso de gestión de usuarios y celdas (actor: Operador).", "62%")
             + "</section>")
    hu = "".join(
        f"<div class='hu'><b>{i} — {t}</b><br><b>Como</b> {r}, <b>quiero</b> {q}, <b>para</b> {p}.<ol>"
        + "".join(f"<li>{c}</li>" for c in crit) + "</ol></div>" for i, t, r, q, p, crit in HU)
    s.append("<section><h2>3. Historias de usuario</h2>" + hu + "</section>")
    s.append("<section><h2>4. Mockups</h2>"
             + fig("mockup-clientes-listado.png", "Figura 2. Listado y búsqueda de clientes (HU-04, HU-05).")
             + fig("mockup-cliente-formulario.png", "Figura 3. Formulario de cliente con rechazo por duplicidad (HU-04).")
             + fig("mockup-vehiculo-formulario.png", "Figura 4. Registro de vehículos de un cliente (HU-06).")
             + fig("mockup-celdas-gestion.png", "Figura 5. Gestión de celdas (HU-07, HU-08, HU-09).") + "</section>")
    s.append("<section><h2>5. Diagramas de secuencia</h2><h3>Iteración 1</h3>"
             + fig("seq-entrada.png", "Figura 6. Registrar entrada de vehículo.", "85%")
             + fig("seq-salida.png", "Figura 7. Registrar salida de vehículo.", "85%")
             + "<h3>Iteración 2</h3>"
             + fig("seq-cliente.png", "Figura 8. Registrar cliente.", "85%")
             + fig("seq-vehiculo.png", "Figura 9. Registrar vehículo de un cliente.", "85%")
             + fig("seq-celda.png", "Figura 10. Crear, editar o eliminar celda.", "85%") + "</section>")
    s.append("<section><h2>6. Diagrama de componentes</h2>"
             + fig("componentes.png", "Figura 11. Componentes del sistema (iteraciones 1 y 2).", "80%") + "</section>")
    s.append("<section class='wide'><h2>7. Plan de pruebas resumido</h2>"
             + table(["ID", "HU", "Caso de prueba", "Datos de entrada", "Resultado esperado", "Resultado obtenido", "Estado"], pruebas_rows())
             + "<p class='small'>Las pruebas automatizadas están en <code>autos-colombia/src/test</code> y se ejecutan con <code>./mvnw test</code>.</p></section>")
    s.append(f"<section><h2>8. Repositorio y video</h2><p><b>Repositorio GitHub:</b> {REPO}</p>"
             f"<p><b>Video en YouTube:</b> {VIDEO_URL}</p></section>")
    s.append("""<section><h2>Conclusiones</h2><p>La Iteración 2 completa el ciclo de datos maestros del parqueadero: ahora es posible registrar clientes y vehículos y administrar las celdas antes de operar entradas y salidas. Las reglas de integridad (duplicados, celdas ocupadas, clientes con estancia abierta) se aplican en el backend y se comunican al operador con mensajes claros.</p>
<p>La arquitectura por capas, el bloqueo pesimista y los índices únicos mantienen la consistencia ante operaciones simultáneas, y las pruebas automatizadas documentan el comportamiento esperado. Como trabajo futuro quedan la gestión de operadores, los pagos y las mensualidades de la siguiente iteración.</p></section>""")
    css = """body{font-family:Segoe UI,Arial;font-size:11pt;color:#111;line-height:1.45}
h2{color:#0b2f5e;border-bottom:2px solid #0b2f5e;padding-bottom:4px}h3{color:#0b2f5e}
section{page-break-after:always}.cover{text-align:center}.cover h1{font-size:22pt;color:#0b2f5e}.big{font-size:15pt}.sp{height:70px}.small{font-size:9pt;color:#555}
table{width:100%;border-collapse:collapse;font-size:9.5pt;margin:8px 0}th{background:#0b2f5e;color:#fff;text-align:left}th,td{border:1px solid #cbd5e1;padding:5px 7px;vertical-align:top}
figure{margin:10px 0;text-align:center;page-break-inside:avoid}figcaption{font-size:9pt;color:#555}.hu{border-left:4px solid #1d4ed8;background:#f1f5f9;padding:8px 12px;margin:10px 0;page-break-inside:avoid}
.hu ol{margin:4px 0 0 18px;padding:0}.wide table{font-size:7.5pt}tr{page-break-inside:avoid}.wide{page-break-after:auto}"""
    return f"<html><head><meta charset='utf-8'><style>{css}</style></head><body>{''.join(s)}</body></html>"


if __name__ == "__main__":
    html = build_html()
    (HERE / "informe.html").write_text(html, encoding="utf-8")
    with sync_playwright() as p:
        b = p.chromium.launch(channel="chrome")
        pg = b.new_page()
        pg.set_content(html)
        pg.pdf(path=str(OUT), format="A4", print_background=True,
               display_header_footer=True, header_template="<span></span>",
               footer_template="<div style='font-size:8px;width:100%;text-align:center'>Grupo 18 · Página <span class='pageNumber'></span></div>",
               margin={"top": "18mm", "bottom": "18mm", "left": "16mm", "right": "16mm"})
        b.close()
    print("PDF:", OUT)
