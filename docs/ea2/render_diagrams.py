"""Renderiza los .mmd de src/ a PNG (docs/ea2/img) con Mermaid + Chromium (Playwright)."""
import pathlib
from playwright.sync_api import sync_playwright
SRC = pathlib.Path(__file__).parent / "src"; IMG = pathlib.Path(__file__).parent / "img"
with sync_playwright() as p:
    b = p.chromium.launch(channel="chrome"); pg = b.new_page(device_scale_factor=2)
    for f in sorted(SRC.glob("*.mmd")):
        code = f.read_text(encoding="utf-8")
        html = ("<html><body style='margin:0;background:#fff'><pre class='mermaid' style='padding:16px'>" + code +
                "</pre><script type='module'>import m from 'https://cdn.jsdelivr.net/npm/mermaid@10.9.1/dist/mermaid.esm.min.mjs';"
                "m.initialize({startOnLoad:false,theme:'neutral',themeVariables:{fontFamily:'Segoe UI,Arial'}});"
                "await m.run();document.body.dataset.done=1;</script></body></html>")
        pg.set_content(html); pg.wait_for_selector("body[data-done]", timeout=60000)
        pg.locator("svg").first.screenshot(path=str(IMG / (f.stem + ".png")))
        print("ok", f.stem)
    b.close()
