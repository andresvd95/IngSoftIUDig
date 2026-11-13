"""Genera el caso de uso (SVG->PNG) y los mockups de la iteración 2 (HTML->PNG) en docs/ea2/img."""
import pathlib
from playwright.sync_api import sync_playwright

IMG = pathlib.Path(__file__).parent / "img"

# ---------- Diagrama de casos de uso ----------
cases = [("CU-01 Registrar cliente", 130), ("CU-02 Consultar / buscar clientes", 200), ("CU-03 Editar cliente", 270),
         ("CU-04 Eliminar cliente", 340), ("CU-05 Registrar vehículo del cliente", 410),
         ("CU-06 Crear celda", 520), ("CU-07 Editar celda", 590), ("CU-08 Eliminar celda", 660),
         ("CU-09 Consultar estado de celdas", 730)]
svg = ['<svg xmlns="http://www.w3.org/2000/svg" width="900" height="820" font-family="Segoe UI,Arial" font-size="14">',
       '<rect width="900" height="820" fill="#fff"/>',
       '<rect x="250" y="30" width="600" height="770" fill="#f8fafc" stroke="#0b2f5e" stroke-width="2"/>',
       '<text x="550" y="58" text-anchor="middle" font-weight="bold" fill="#0b2f5e" font-size="16">Sistema Autos Colombia · Iteración 2</text>',
       '<text x="550" y="92" text-anchor="middle" fill="#0b2f5e" font-size="13">Gestión de usuarios (clientes y vehículos)</text>',
       '<text x="550" y="482" text-anchor="middle" fill="#0b2f5e" font-size="13">Gestión de celdas</text>']
for t, y in cases:
    svg.append(f'<line x1="130" y1="400" x2="360" y2="{y}" stroke="#475569"/>')
    svg.append(f'<ellipse cx="550" cy="{y}" rx="190" ry="27" fill="#fff" stroke="#0b2f5e" stroke-width="1.5"/>'
               f'<text x="550" y="{y + 5}" text-anchor="middle">{t}</text>')
svg.append('<g stroke="#0b2f5e" stroke-width="2" fill="none"><circle cx="100" cy="370" r="16"/>'
           '<line x1="100" y1="386" x2="100" y2="440"/><line x1="70" y1="405" x2="130" y2="405"/>'
           '<line x1="100" y1="440" x2="75" y2="485"/><line x1="100" y1="440" x2="125" y2="485"/></g>'
           '<text x="100" y="510" text-anchor="middle" font-weight="bold">Operador</text></svg>')
(IMG / "casos-uso-iteracion2.svg").write_text("\n".join(svg), encoding="utf-8")

# ---------- Mockups ----------
CSS = """body{margin:0;font-family:Segoe UI,Arial;background:#eef2f7;color:#0f172a}
.side{position:fixed;left:0;top:0;bottom:0;width:210px;background:#0b2f5e;color:#cbd5e1;padding:20px 0}
.side b{display:block;color:#fff;padding:0 20px 20px;font-size:18px}.side div{padding:12px 20px}.side .on{background:#1d4ed8;color:#fff}
.main{margin-left:210px;padding:28px 36px}h1{margin:0 0 4px;font-size:24px}.sub{color:#64748b;margin-bottom:20px}
.card{background:#fff;border-radius:10px;padding:20px;box-shadow:0 1px 3px #0002;margin-bottom:18px}
table{width:100%;border-collapse:collapse}th{text-align:left;color:#64748b;font-size:12px;text-transform:uppercase;padding:8px;border-bottom:2px solid #e2e8f0}
td{padding:10px 8px;border-bottom:1px solid #e2e8f0}.btn{display:inline-block;background:#1d4ed8;color:#fff;padding:9px 16px;border-radius:7px;font-size:14px}
.btn.g{background:#fff;color:#1d4ed8;border:1px solid #1d4ed8}.btn.r{background:#dc2626}.inp{border:1px solid #cbd5e1;border-radius:7px;padding:9px;background:#fff;color:#64748b;min-width:200px;display:inline-block}
label{display:block;font-size:12px;color:#475569;margin:12px 0 4px}.row{display:flex;gap:16px}.row>div{flex:1}.inp.f{display:block}
.pill{padding:3px 10px;border-radius:99px;font-size:12px}.ok{background:#dcfce7;color:#166534}.no{background:#fee2e2;color:#991b1b}
.err{background:#fee2e2;color:#991b1b;border-left:4px solid #dc2626;padding:10px 14px;border-radius:6px;margin-bottom:14px}"""


def side(on):
    items = ["Resumen", "Registrar entrada", "Registrar salida", "Clientes", "Celdas", "Historial"]
    return "<div class='side'><b>Autos Colombia</b>" + "".join(
        f"<div class='{'on' if i == on else ''}'>{i}</div>" for i in items) + "</div>"


ACC = "<span class='btn g'>Editar</span> <span class='btn g'>Vehículos</span> <span class='btn r'>Eliminar</span>"
pages = {
    "mockup-clientes-listado": side("Clientes") + f"""<div class='main'><h1>Clientes</h1><div class='sub'>HU-04 · HU-05 · Gestión de usuarios</div>
<div class='card'><div class='row' style='align-items:center'><span class='inp' style='flex:3'>Buscar por nombre o identificación</span><span class='btn' style='flex:0 0 auto'>+ Nuevo cliente</span></div></div>
<div class='card'><table><tr><th>Tipo</th><th>Identificación</th><th>Nombre</th><th>Teléfono</th><th>Vehículos</th><th>Acciones</th></tr>
<tr><td>CC</td><td>1020304050</td><td>María Gómez</td><td>3001234567</td><td>2</td><td>{ACC}</td></tr>
<tr><td>CC</td><td>7080901020</td><td>Carlos Pérez</td><td>3109876543</td><td>1</td><td>{ACC}</td></tr>
<tr><td>CE</td><td>9988776655</td><td>Ana Ruiz</td><td>3015556677</td><td>1</td><td>{ACC}</td></tr></table>
<div style='margin-top:12px;color:#64748b;font-size:13px'>Mostrando 3 de 3 clientes</div></div></div>""",
    "mockup-cliente-formulario": side("Clientes") + """<div class='main'><h1>Registrar cliente</h1><div class='sub'>HU-04 · Validaciones y rechazo por duplicidad</div>
<div class='err'>Ya existe un cliente con esa identificación (HTTP 409).</div>
<div class='card'><div class='row'><div><label>Tipo de identificación *</label><span class='inp f'>CC ▾</span></div><div><label>Número de identificación *</label><span class='inp f'>1020304050</span></div></div>
<label>Nombre completo *</label><span class='inp f'>María Gómez</span>
<div class='row'><div><label>Correo electrónico</label><span class='inp f'>maria@correo.com</span></div><div><label>Teléfono</label><span class='inp f'>3001234567</span></div></div>
<div style='margin-top:20px'><span class='btn'>Guardar cliente</span> <span class='btn g'>Cancelar</span></div></div></div>""",
    "mockup-vehiculo-formulario": side("Clientes") + """<div class='main'><h1>Vehículos de María Gómez</h1><div class='sub'>HU-06 · Registrar vehículo del cliente</div>
<div class='card'><table><tr><th>Placa</th><th>Tipo</th><th>Marca</th><th>Modelo</th><th>Color</th></tr>
<tr><td>ABC-123</td><td>Carro</td><td>Mazda</td><td>2020</td><td>Rojo</td></tr><tr><td>XYZ-45A</td><td>Moto</td><td>Yamaha</td><td>2022</td><td>Negro</td></tr></table></div>
<div class='card'><b>Nuevo vehículo</b><div class='row'><div><label>Placa *</label><span class='inp f'>DEF-789</span></div><div><label>Tipo de vehículo *</label><span class='inp f'>Carro ▾</span></div></div>
<div class='row'><div><label>Marca</label><span class='inp f'>Renault</span></div><div><label>Modelo</label><span class='inp f'>2021</span></div><div><label>Color</label><span class='inp f'>Gris</span></div></div>
<div style='margin-top:20px'><span class='btn'>Registrar vehículo</span></div></div></div>""",
    "mockup-celdas-gestion": side("Celdas") + """<div class='main'><h1>Gestión de celdas</h1><div class='sub'>HU-07 · HU-08 · HU-09</div>
<div class='card'><div class='row' style='align-items:end'><div><label>Número de celda *</label><span class='inp f'>A-12</span></div><div><label>Tipo de vehículo *</label><span class='inp f'>Carro ▾</span></div><div style='flex:0 0 auto'><span class='btn'>Crear celda</span></div></div></div>
<div class='card'><table><tr><th>Celda</th><th>Tipo</th><th>Estado</th><th>Acciones</th></tr>
<tr><td>A-01</td><td>Carro</td><td><span class='pill ok'>Disponible</span></td><td><span class='btn g'>Editar</span> <span class='btn r'>Eliminar</span></td></tr>
<tr><td>A-02</td><td>Carro</td><td><span class='pill no'>Ocupada</span></td><td><span class='btn g' style='opacity:.4'>Editar</span> <span class='btn r' style='opacity:.4'>Eliminar</span></td></tr>
<tr><td>M-01</td><td>Moto</td><td><span class='pill ok'>Disponible</span></td><td><span class='btn g'>Editar</span> <span class='btn r'>Eliminar</span></td></tr></table>
<div style='margin-top:12px;color:#64748b;font-size:13px'>Una celda ocupada no se puede editar ni eliminar (HTTP 409).</div></div></div>""",
}
with sync_playwright() as p:
    b = p.chromium.launch(channel="chrome")
    pg = b.new_page(viewport={"width": 1180, "height": 600}, device_scale_factor=1.5)
    for name, body in pages.items():
        pg.set_content(f"<html><head><meta charset='utf-8'><style>{CSS}</style></head><body>{body}</body></html>")
        pg.screenshot(path=str(IMG / f"{name}.png"))
    pg.set_viewport_size({"width": 900, "height": 820})
    pg.set_content(f"<html><body style='margin:0'>{(IMG / 'casos-uso-iteracion2.svg').read_text(encoding='utf-8')}</body></html>")
    pg.screenshot(path=str(IMG / "casos-uso-iteracion2.png"))
    b.close()
print("ok")
