# 🎨 Design System - Sports League Manager

## Color Palette

### Primary Colors
- **Main Gradient**: `#667eea` (Purple) → `#764ba2` (Darker Purple)
- **Background Gradient**: `#f5f7fa` (Light Gray) → `#c3cfe2` (Light Blue)

### Status Colors
- **Success**: `#51cf66` (Green)
- **Danger**: `#ff6b6b` (Red)
- **Warning**: `#ffd93d` (Yellow)
- **Info**: `#4ecdc4` (Teal)

### Neutral Colors
- **Dark Text**: `#333333`
- **Medium Text**: `#555555`
- **Light Text**: `#999999`
- **Border**: `#e0e6ed`
- **Light Background**: `#f8f9fc`
- **White**: `#ffffff`

## Typography

### Font Family
```css
font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
```

### Font Sizes
- **Page Title**: 28px (font-weight: 700)
- **Card Title**: 18px (font-weight: 600)
- **Label**: 14px (font-weight: 600)
- **Body Text**: 14px (font-weight: 400)
- **Small Text**: 12px (font-weight: 400)
- **Icon**: 16px - 48px (varies by context)

## Components

### Navbar
```css
Height: 80px
Background: Linear gradient (667eea → 764ba2)
Shadow: 0 4px 20px rgba(0, 0, 0, 0.1)
Position: Fixed (top)
Padding: 0 40px
Z-index: 1000
```

**Navbar Brand**
- Font Size: 24px
- Font Weight: 700
- Color: White
- Icon Size: 28px
- Gap: 12px

**Navigation Buttons**
- Font Size: 14px
- Font Weight: 600
- Padding: 8px 16px
- Border Radius: 10px
- Gap between icon and text: 8px
- Hover: Background 20% white, translateY(-2px)
- Active: Background 30% white, box-shadow

### Cards
```css
Border Radius: 16px
Padding: 30px
Background: White
Box Shadow: 0 8px 24px rgba(0, 0, 0, 0.08)
Margin Bottom: 30px
Transition: all 0.3s ease

On Hover:
  Box Shadow: 0 12px 32px rgba(0, 0, 0, 0.12)
  Transform: translateY(-4px)
```

### Form Inputs
```css
Padding: 12px 16px
Border: 2px solid #e0e6ed
Border Radius: 10px
Font Size: 14px
Transition: all 0.3s ease

On Focus:
  Border Color: #667eea
  Box Shadow: 0 0 0 4px rgba(102, 126, 234, 0.1)
```

### Buttons
```css
Base:
  Padding: 12px 24px
  Border: None
  Border Radius: 10px
  Font Size: 14px
  Font Weight: 600
  Cursor: pointer
  Display: flex
  Align Items: center
  Gap: 8px
  Transition: all 0.3s ease

Primary:
  Background: Linear gradient (667eea → 764ba2)
  Color: White
  On Hover: translateY(-2px), box-shadow

Secondary:
  Background: #f0f3f8
  Color: #667eea
  On Hover: Background #e0e6ed

Danger:
  Background: #ff6b6b
  Color: White
  Padding: 8px 16px
  Font Size: 13px
  On Hover: Background #ff5252, box-shadow

Icon Buttons:
  Width: 40px
  Height: 40px
  Border Radius: 10px
  Font Size: 16px
```

### Tables
```css
Border Collapse: collapse
Margin Top: 20px

Header:
  Background: #f8f9fc
  Padding: 16px
  Font Weight: 600
  Color: #333
  Font Size: 14px
  Border Bottom: 2px solid #e0e6ed

Cells:
  Padding: 16px
  Border Bottom: 1px solid #e0e6ed
  Font Size: 14px
  Color: #555

Row Hover:
  Background: #f8f9fc
```

### Alerts
```css
Padding: 16px 20px
Border Radius: 10px
Margin Bottom: 20px
Display: flex
Align Items: center
Gap: 12px
Font Size: 14px
Animation: slideDown 0.3s ease
Auto-dismiss: 4 seconds

Success:
  Background: #d4edda
  Color: #155724
  Border: 1px solid #c3e6cb

Danger:
  Background: #f8d7da
  Color: #721c24
  Border: 1px solid #f5c6cb

Info:
  Background: #d1ecf1
  Color: #0c5460
  Border: 1px solid #bee5eb
```

### Badges
```css
Padding: 4px 8px
Border Radius: 6px
Font Size: 12px
Font Weight: 600

Default (blue):
  Background: #e3f2fd
  Color: #1976d2

Success (green):
  Background: #d4edda
  Color: #155724

Danger (red):
  Background: #f8d7da
  Color: #721c24

Warning (yellow):
  Background: #fff3cd
  Color: #856404
```

## Spacing Scale

```
4px   - xs
8px   - sm
12px  - md
16px  - lg
20px  - xl
24px  - 2xl
30px  - 3xl
40px  - 4xl
```

## Border Radius Scale

```
6px   - sm (badges)
10px  - md (inputs, buttons, icon buttons)
12px  - lg (cards on mobile)
16px  - xl (cards)
50%   - full (avatars, spinners)
```

## Shadows

```
sm:   0 1px 2px rgba(0, 0, 0, 0.05)
md:   0 4px 6px rgba(0, 0, 0, 0.1)
lg:   0 8px 24px rgba(0, 0, 0, 0.08)
xl:   0 12px 32px rgba(0, 0, 0, 0.12)
2xl:  0 20px 40px rgba(0, 0, 0, 0.15)

Navbar: 0 4px 20px rgba(0, 0, 0, 0.1)
Focus:  0 0 0 4px rgba(102, 126, 234, 0.1)
Hover:  0 8px 16px rgba(102, 126, 234, 0.3)
```

## Animations

### Fade In
```css
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
Duration: 0.3s
Easing: ease
```

### Slide Down
```css
@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
Duration: 0.3s
Easing: ease
```

### Spin (Loading)
```css
@keyframes spin {
  to { transform: rotate(360deg); }
}
Duration: 0.6s
Easing: linear
Infinite
```

## Responsive Breakpoints

```
Mobile:  0px - 480px
Tablet:  481px - 768px
Desktop: 769px+
```

### Navbar Responsive
- **Desktop**: Full navbar with text and icons
- **Tablet**: Condensed spacing
- **Mobile**: Icon-only buttons except active

### Form Grid Responsive
- **Desktop**: `repeat(auto-fit, minmax(250px, 1fr))`
- **Tablet**: 2-3 columns
- **Mobile**: 1 column

### Table Responsive
- **Desktop**: Full table
- **Mobile**: Scrollable with reduced padding

## Icon Guide

### Font Awesome Icons Used

| Icon | Code | Usage |
|------|------|-------|
| Users | `fas fa-users` | Players section |
| Shield | `fas fa-shield` | Teams section |
| Football | `fas fa-futbol` | Matches section |
| Star | `fas fa-ranking-star` | Leaderboard |
| User Tie | `fas fa-user-tie` | Assignments |
| Crown | `fas fa-crown` | Brand logo |
| Plus Circle | `fas fa-plus-circle` | Add/Create action |
| Redo | `fas fa-redo` | Reset/Clear |
| Trash Alt | `fas fa-trash-alt` | Delete action |
| Check Circle | `fas fa-check-circle` | Success alert |
| Exclamation | `fas fa-exclamation-circle` | Error alert |
| Info | `fas fa-info-circle` | Info message |
| Link | `fas fa-link` | Assign/Connect |
| Times | `fas fa-times` | Close button |

All icons use Font Awesome Free v6.4.0

## Accessibility

- **Color Contrast**: All text meets WCAG AA standards
- **Focus States**: Clear focus indicators on interactive elements
- **Semantic HTML**: Proper heading hierarchy and labels
- **ARIA**: Proper ARIA labels where needed
- **Keyboard Navigation**: All interactive elements accessible via keyboard

## Performance Considerations

- **No External Fonts**: Uses system fonts for better performance
- **Minimal Animations**: 0.3s animations for smooth experience
- **Efficient Shadows**: Uses minimal shadow layers
- **Optimized Icons**: SVG icons from Font Awesome
- **Mobile-First**: Base styles for mobile, enhanced for desktop

## Brand Voice

- **Modern**: Contemporary design with smooth transitions
- **Professional**: Clean, organized, business-appropriate
- **Friendly**: Approachable with subtle animations
- **Efficient**: Minimal clutter, clear hierarchy
- **Consistent**: Unified design language throughout

