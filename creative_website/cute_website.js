

let flowers = document.getElementById('flowers');
let text = document.getElementById('text');
let header = document.getElementById('header');
let btn = document.getElementById('btn');

window.addEventListener('scroll',function(){
    let value = window.scrollY;
    flowers.style.top = value * 0.05 + 'px';
    text.style.marginRight = value * 5 + 'px';
    btn.style.marginTop = value * 1.25 + 'px';
    header.style.top = value * 0.5 + 'px';
})
